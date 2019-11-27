package com.example.assignment2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class Accelerometer implements SensorEventListener {

    private SensorManager sensorManager;
    private float deltaY = 0;
    private Context myContext;
    private Sensor accelerometer;
    private PingView pingView;

    public Accelerometer(Context context, PingView pingView){
        myContext = context;
        this.pingView = pingView;
    }

    public void sensor(){
        sensorManager = (SensorManager) myContext.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            Log.i("deltaY", "wohooo");
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        deltaY = Math.abs(deltaY - event.values[1]);
        if (deltaY > 5){
            Log.i("deltaY", ""+event.values[1]*290);
            pingView.movePaddle(event.values[1]*290, pingView.getPlayer1());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
