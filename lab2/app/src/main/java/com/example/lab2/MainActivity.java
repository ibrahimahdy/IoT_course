package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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


}
