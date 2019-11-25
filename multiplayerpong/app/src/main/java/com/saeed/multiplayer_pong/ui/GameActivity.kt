package com.saeed.multiplayer_pong.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.saeed.multiplayer_pong.R
import com.saeed.multiplayer_pong.engine.GameState
import com.saeed.multiplayer_pong.engine.GameThread
import kotlinx.android.synthetic.main.game_activity.*


/**
 * Created by Saeed on 24/11/2019.
 */
class GameActivity : AppCompatActivity() {

    private lateinit var gameThread: GameThread
    private lateinit var sensorManager: SensorManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_activity)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        game_view.setScoreView(score)
        game_view.setStatusView(status)

        gameThread = game_view.getGameThread()

        if (savedInstanceState != null) {
            gameThread.restoreState(savedInstanceState)
        } else {
            gameThread.setState(GameState.READY)
        }


        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

    }

    override fun onResume() {
        super.onResume()
        val sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(
            game_view,
            sensorAccelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        game_view.sensorAccelerometer_ = sensorAccelerometer
        gameThread.startNewGame()
    }

    override fun onPause() {
        super.onPause()
        gameThread.pause()
        sensorManager.unregisterListener(game_view)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        gameThread.saveState(outState)
    }


}