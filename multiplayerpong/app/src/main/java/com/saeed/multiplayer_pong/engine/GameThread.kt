package com.saeed.multiplayer_pong.engine

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.View
import com.saeed.multiplayer_pong.R
import java.util.*
import kotlin.math.cos
import kotlin.math.sign
import kotlin.math.sin


/**
 * Created by Saeed on 24/11/2019.
 */
class GameThread(
    private val surfaceHolder: SurfaceHolder,
    private val context: Context,
    private val statusHandler: Handler,
    private val scoreHandler: Handler,
    private val attributeSet: AttributeSet
) : Thread() {

    private var gameState = GameState.READY

    private val BALL_SPEED = 8
    private val PADDLE_SPEED = 8
    private val FPS = 60
    private val MAX_BOUNCE_ANGLE = 5 * Math.PI / 12
    private val COLLISION_FRAMES = 5

    private val KEY_HUMAN_PLAYER_DATA = "humanPlayer"
    private val KEY_COMPUTER_PLAYER_DATA = "computerPlayer"
    private val KEY_BALL_DATA = "ball"
    private val KEY_GAME_STATE = "state"

    private val TAG = "PongThread"


    private var run: Boolean = false
    private val runLock: Any


    private val humanPlayer: Player
    private val computerPlayer: Player
    private val ball: Ball

    private val medianLinePaint: Paint

    private val canvasBoundsPaint: Paint
    private var canvasHeight: Int = 0
    private var canvasWidth: Int = 0

    /**
     * Used to make computer to "forget" to move the paddle in order to behave more like a human opponent.
     */
    private val mRandomGen: Random

    /**
     * The probability to move computer paddle.
     */
    private val mComputerMoveProbability: Float

    init {
        run = false
        runLock = Any()

        val a = context.obtainStyledAttributes(attributeSet, R.styleable.PongView)

        val paddleHeight = a.getInt(R.styleable.PongView_paddleHeight, 100)
        val paddleWidth = a.getInt(R.styleable.PongView_paddleWidth, 15)
        val ballRadius = a.getInt(R.styleable.PongView_ballRadius, 20)

        a.recycle()

        val humanPlayerPaint = Paint()
        humanPlayerPaint.isAntiAlias = true
        humanPlayerPaint.color = Color.parseColor("#009688")

        humanPlayer = Player(paddleWidth, paddleHeight, humanPlayerPaint)

        val computerPlayerPaint = Paint()
        computerPlayerPaint.isAntiAlias = true
        computerPlayerPaint.color = Color.parseColor("#F44336")

        computerPlayer = Player(paddleWidth, paddleHeight, computerPlayerPaint)

        val ballPaint = Paint()
        ballPaint.isAntiAlias = true
        ballPaint.color = Color.parseColor("#9C27B0")

        ball = Ball(ballRadius, ballPaint)


        medianLinePaint = Paint()
        medianLinePaint.isAntiAlias = true
        medianLinePaint.color = Color.YELLOW
        medianLinePaint.alpha = 80
        medianLinePaint.style = Paint.Style.FILL_AND_STROKE
        medianLinePaint.strokeWidth = 2.0f
        medianLinePaint.pathEffect = DashPathEffect(floatArrayOf(5f, 5f), 0f)

        canvasBoundsPaint = Paint()
        canvasBoundsPaint.isAntiAlias = true
        canvasBoundsPaint.color = Color.YELLOW
        canvasBoundsPaint.style = Paint.Style.STROKE
        canvasBoundsPaint.strokeWidth = 1.0f

        canvasHeight = 1
        canvasWidth = 1

        mRandomGen = Random()
        mComputerMoveProbability = 0.6f
    }

    override fun run() {
        super.run()
        var mNextGameTick = SystemClock.uptimeMillis()
        val skipTicks = 1000 / FPS
        while (run) {
            var c: Canvas? = null
            try {
                c = surfaceHolder.lockCanvas(null)
                if (c != null) {
                    synchronized(surfaceHolder) {
                        if (gameState == GameState.RUNNING) {
                            updatePhysics()
                        }
                        synchronized(runLock) {
                            if (run) {
                                updateDisplay(c)
                            }
                        }
                    }
                }
            } finally {
                if (c != null) {
                    surfaceHolder.unlockCanvasAndPost(c)
                }
            }
            mNextGameTick += skipTicks.toLong()
            val sleepTime = mNextGameTick - SystemClock.uptimeMillis()
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime)
                } catch (e: InterruptedException) {
                    Log.e(TAG, "Interrupted", e)
                }

            }
        }
    }

    internal fun setRunning(running: Boolean) {
        synchronized(runLock) {
            run = running
        }
    }

    internal fun saveState(map: Bundle) {
        synchronized(surfaceHolder) {
            map.putFloatArray(
                KEY_HUMAN_PLAYER_DATA,
                floatArrayOf(
                    humanPlayer.bounds.left,
                    humanPlayer.bounds.top,
                    humanPlayer.score.toFloat()
                )
            )

            map.putFloatArray(
                KEY_COMPUTER_PLAYER_DATA,
                floatArrayOf(
                    computerPlayer.bounds.left,
                    computerPlayer.bounds.top,
                    computerPlayer.score.toFloat()
                )
            )

            map.putFloatArray(
                KEY_BALL_DATA,
                floatArrayOf(ball.cx, ball.cy, ball.dx, ball.dy)
            )

            map.putString(KEY_GAME_STATE, gameState.name)
        }
    }

    internal fun restoreState(map: Bundle) {
        synchronized(surfaceHolder) {
            val humanPlayerData = map.getFloatArray(KEY_HUMAN_PLAYER_DATA)
            humanPlayer.score = humanPlayerData!![2].toInt()
            movePlayer(humanPlayer, humanPlayerData[0], humanPlayerData[1])

            val computerPlayerData = map.getFloatArray(KEY_COMPUTER_PLAYER_DATA)
            computerPlayer.score = computerPlayerData!![2].toInt()
            movePlayer(computerPlayer, computerPlayerData[0], computerPlayerData[1])

            val ballData = map.getFloatArray(KEY_BALL_DATA)
            ball.cx = ballData!![0]
            ball.cy = ballData[1]
            ball.dx = ballData[2]
            ball.dy = ballData[3]

            val state = map.getString(KEY_GAME_STATE, GameState.READY.name)
            setState(GameState.valueOf(state))
        }
    }

    internal fun setState(state: GameState) {
        //    PAUSE(1),READY(2),RUNNING(3),LOSE(4), WIN(5)
        synchronized(surfaceHolder) {
            gameState = state
            val res = context.resources
            when (gameState) {
                GameState.READY -> setupNewRound()
                GameState.RUNNING -> hideStatusText()
                GameState.WIN -> {
                    setStatusText(res.getString(R.string.mode_win))
                    humanPlayer.score++
                    setupNewRound()
                }
                GameState.LOSE -> {
                    setStatusText(res.getString(R.string.mode_lose))
                    computerPlayer.score++
                    setupNewRound()
                }
                GameState.PAUSE -> setStatusText(res.getString(R.string.mode_pause))
            }
        }
    }

    internal fun pause() {
        synchronized(surfaceHolder) {
            if (gameState == GameState.RUNNING) {
                setState(GameState.PAUSE)
            }
        }
    }

    internal fun unPause() {
        synchronized(surfaceHolder) {
            setState(GameState.RUNNING)
        }
    }

    /**
     * Reset score and start new game.
     */
    internal fun startNewGame() {
        synchronized(surfaceHolder) {
            humanPlayer.score = 0
            computerPlayer.score = 0
            setupNewRound()
            setState(GameState.RUNNING)
        }
    }

    internal fun isBetweenRounds(): Boolean {
        return gameState != GameState.RUNNING
    }

    internal fun isTouchOnHumanPaddle(event: MotionEvent): Boolean {
        return humanPlayer.bounds.contains(event.x, event.y)
    }

    internal fun moveHumanPaddle(dy: Float) {
        Log.v("Move ", "Move = " + dy)
        if(gameState != GameState.RUNNING){
            return
        }
        synchronized(surfaceHolder) {
            movePlayer(
                humanPlayer,
                humanPlayer.bounds.left,
                humanPlayer.bounds.top + dy
            )
        }
    }

    internal fun setSurfaceSize(width: Int, height: Int) {
        synchronized(surfaceHolder) {
            canvasWidth = width
            canvasHeight = height
            setupNewRound()
        }
    }

    private fun doAI() {
        if (computerPlayer.bounds.top > ball.cy) {
            // move up
            movePlayer(
                computerPlayer,
                computerPlayer.bounds.left,
                computerPlayer.bounds.top - PADDLE_SPEED
            )
        } else if (computerPlayer.bounds.top + computerPlayer.paddleHeight < ball.cy) {
            // move down
            movePlayer(
                computerPlayer,
                computerPlayer.bounds.left,
                computerPlayer.bounds.top + PADDLE_SPEED
            )
        }
    }

    /**
     * Update paddle and player positions, check for collisions, win or lose.
     */
    private fun updatePhysics() {

        if (humanPlayer.collision > 0) {
            humanPlayer.collision--
        }
        if (computerPlayer.collision > 0) {
            computerPlayer.collision--
        }

        doAI()
        when {
            collision(humanPlayer, ball) -> {
                handleCollision(humanPlayer, ball)
                humanPlayer.collision = COLLISION_FRAMES
            }
            collision(computerPlayer, ball) -> {
                handleCollision(computerPlayer, ball)
                computerPlayer.collision = COLLISION_FRAMES
            }
            ballCollidedWithTopOrBottomWall() -> ball.dy = -ball.dy
            ballCollidedWithRightWall() -> {
                setState(GameState.WIN)    // human plays on left
                return
            }
            ballCollidedWithLeftWall() -> {
                setState(GameState.LOSE)
                return
            }
        }

        if (mRandomGen.nextFloat() < mComputerMoveProbability) {
            doAI()
        }

        moveBall()
    }

    private fun moveBall() {
        ball.cx += ball.dx
        ball.cy += ball.dy

        if (ball.cy < ball.radius) {
            ball.cy = ball.radius.toFloat()
        } else if (ball.cy.plus(ball.radius) >= canvasHeight) {
            ball.cy = canvasHeight.minus(ball.radius.minus(1)).toFloat()
        }
    }


    private fun ballCollidedWithLeftWall(): Boolean {
        return ball.cx <= ball.radius
    }

    private fun ballCollidedWithRightWall(): Boolean {
        return (ball.cx.plus(ball.radius)) >= (canvasWidth.minus(1))
    }

    private fun ballCollidedWithTopOrBottomWall(): Boolean {
        return ball.cy <= ball.radius || (ball.cy.plus(ball.radius) >= (canvasHeight.minus(1)))
    }

    /**
     * Draws the score, paddles and the ball.
     */
    private fun updateDisplay(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)
        canvas.drawRect(0f, 0f, canvasWidth.toFloat(), canvasHeight.toFloat(), canvasBoundsPaint)

        val middle = canvasWidth / 2
        canvas.drawLine(
            middle.toFloat(),
            1f,
            middle.toFloat(),
            (canvasHeight - 1).toFloat(),
            medianLinePaint
        )

        setScoreText(humanPlayer.score.toString() + "    " + computerPlayer.score)

        handleHit(humanPlayer)
        handleHit(computerPlayer)

        canvas.drawRoundRect(humanPlayer.bounds, 5f, 5f, humanPlayer.paint)
        canvas.drawRoundRect(computerPlayer.bounds, 5f, 5f, computerPlayer.paint)
        canvas.drawCircle(ball.cx, ball.cy, ball.radius.toFloat(), ball.paint)
    }

    private fun handleHit(player: Player) {
        if (player.collision > 0) {
            player.paint.setShadowLayer(
                (player.paddleWidth / 2).toFloat(),
                0F,
                0F,
                player.paint.color
            )
        } else {
            player.paint.setShadowLayer(0F, 0F, 0F, 0)
        }
    }

    /**
     * Reset players and ball position for a new round.
     */
    private fun setupNewRound() {
        ball.cx = (canvasWidth / 2).toFloat()
        ball.cy = (canvasHeight / 2).toFloat()
        ball.dx = -BALL_SPEED.toFloat()
        ball.dy = 0F

        movePlayer(
            humanPlayer,
            2f,
            ((canvasHeight - humanPlayer.paddleHeight) / 2).toFloat()
        )

        movePlayer(
            computerPlayer,
            (canvasWidth - computerPlayer.paddleWidth - 2).toFloat(),
            ((canvasHeight - computerPlayer.paddleHeight) / 2).toFloat()
        )
    }

    private fun setStatusText(text: String) {
        val msg = statusHandler.obtainMessage()
        val b = Bundle()
        b.putString("text", text)
        b.putInt("vis", View.VISIBLE)
        msg.data = b
        statusHandler.sendMessage(msg)
    }

    private fun hideStatusText() {
        val msg = statusHandler.obtainMessage()
        val b = Bundle()
        b.putInt("vis", View.INVISIBLE)
        msg.data = b
        statusHandler.sendMessage(msg)
    }

    private fun setScoreText(text: String) {
        val msg = scoreHandler.obtainMessage()
        val b = Bundle()
        b.putString("text", text)
        msg.data = b
        scoreHandler.sendMessage(msg)
    }

    private fun movePlayer(player: Player, left: Float, top: Float) {
        Log.v("Left ", "Left =" + left)
        Log.v("top ", "top =" + top)
        var left = left
        var top = top
        if (left < 2) {
            left = 2f
        } else if (left + player.paddleWidth >= canvasWidth - 2) {
            left = (canvasWidth - player.paddleWidth - 2).toFloat()
        }
        if (top < 0) {
            top = 0f
        } else if (top + player.paddleHeight >= canvasHeight) {
            top = (canvasHeight - player.paddleHeight - 1).toFloat()
        }
        player.bounds.offsetTo(left, top)
    }

    private fun collision(player: Player, ball: Ball): Boolean {
        return player.bounds.intersects(
            ball.cx - this.ball.radius,
            ball.cy - this.ball.radius,
            ball.cx + this.ball.radius,
            ball.cy + this.ball.radius
        )
    }

    /**
     * Compute ball direction after collision with player paddle.
     */
    private fun handleCollision(player: Player, ball: Ball) {
        val relativeIntersectY = player.bounds.top + player.paddleHeight / 2 - ball.cy
        val normalizedRelativeIntersectY = relativeIntersectY / (player.paddleHeight / 2)
        val bounceAngle = normalizedRelativeIntersectY * MAX_BOUNCE_ANGLE

        ball.dx = -sign(ball.dx) * BALL_SPEED * cos(bounceAngle).toFloat()
        ball.dy = (BALL_SPEED * -sin(bounceAngle)).toFloat()

        if (player === humanPlayer) {
            this.ball.cx = humanPlayer.bounds.right + this.ball.radius
        } else {
            this.ball.cx = computerPlayer.bounds.left - this.ball.radius
        }
    }


}