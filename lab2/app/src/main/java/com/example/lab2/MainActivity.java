package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    EditText tmpValue;
    TextView restlt;
    TextView textMode;



    enum Mode {
        celsius,
        fahrenheit
    }

    public static Mode currentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentMode = Mode.celsius;

        tmpValue = (EditText)findViewById(R.id.tmpTxt);
        restlt = (TextView)findViewById(R.id.restltTxt);
        textMode = (TextView)findViewById(R.id.modeTxt);

        Log.i("MainActivity", "We are onCreate!");

    }

    public void change(View v){

        if(currentMode == Mode.celsius) {
            currentMode = Mode.fahrenheit;
            textMode.setText("Convert to celsius from fahrenheit");
        }
        else {
            currentMode = Mode.celsius;
            textMode.setText("Convert to fahrenheit from celsius");
        }
    }

    public void convert(View v){

        double val = new Double(tmpValue.getText().toString());

        if(currentMode == Mode.celsius) {
            val = Converter.cel2Feh(val);
            restlt.setText("Result: " + val);
        }
        else {
            val = Converter.feh2Cel(val);
            restlt.setText("Result: " + val);
        }

    }




    @Override
    protected void onPause() {
        super.onPause();
        Log.i("OnPauseActivity", "We are onPause!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("onResumeActivity", "We are onResume!");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("onStopActivity", "We are onStop!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("onDestroyeActivity", "We are onDestroy!");
    }

}
