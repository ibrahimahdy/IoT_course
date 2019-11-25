package com.saeed.multiplayer_pong.engine

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener2
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.TextView


/**
 * Created by Saeed on 24/11/2019.
 */
class GameView(context: Context, attributeSet: AttributeSet) :
    SurfaceView(context, attributeSet, 0), SurfaceHolder.Callback2, SensorEventListener2 {
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onFlushCompleted(sensor: Sensor?) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.apply {
            sensor.apply {
                if (type == Sensor.TYPE_ACCELEROMETER) {
                    val dy = lastTouchY - values[0]
                    gameThread.moveHumanPaddle(5 * dy)
                }
            }
        }
    }

    private val gameThread: GameThread

    private var statusView: TextView? = null

    private var scoreView: TextView? = null

    private var moving: Boolean = false

    private var lastTouchY: Float = 0.toFloat()

    private var sensorAccelerometer: Sensor? = null

    var sensorAccelerometer_: Sensor?
        get() = sensorAccelerometer
        set(value) {
            Log.v("Called","Sensor set")
            sensorAccelerometer = value
        }

    init {
        holder.addCallback(this)
        gameThread = GameThread(holder, context, Handler(Handler.Callback {
            statusView?.visibility = it.data.getInt("vis")
            statusView?.text = it.data.getString("text")
            true
        }), Handler(Handler.Callback {
            scoreView?.text = it.data.getString("text")
            true
        }), attributeSet)
    }

    fun setStatusView(textView: TextView) {
        statusView = textView
    }

    fun setScoreView(textView: TextView) {
        scoreView = textView
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        if (!hasWindowFocus) {
            gameThread.pause()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        gameThread.setSurfaceSize(width, height)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        gameThread.setRunning(true)
        gameThread.start()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        gameThread.setRunning(false)
        while (retry) {
            try {
                gameThread.join()
                retry = false
            } catch (e: InterruptedException) {
            }

        }
    }

    override fun surfaceRedrawNeeded(holder: SurfaceHolder) {
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.v("Type ", "type = " + event.action)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> if (gameThread.isBetweenRounds()) {
                // resume game
                gameThread.setState(GameState.RUNNING)
            } else {
                if (gameThread.isTouchOnHumanPaddle(event)) {
                    moving = true
                    lastTouchY = event.y
                }
            }
            /* MotionEvent.ACTION_MOVE -> if (moving) {
                 val y = event.y
                 val dy = y - mLastTouchY
                 mLastTouchY = y

                 gameThread.moveHumanPaddle(dy)
             }*/
            MotionEvent.ACTION_UP -> moving = false
        }
        return true
    }

    fun getGameThread(): GameThread {
        return gameThread
    }

}