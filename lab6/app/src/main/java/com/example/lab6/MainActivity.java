package com.example.lab6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {


    EditText count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        count = (EditText)findViewById(R.id.images_count);


        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra("imgCount", Integer.parseInt(count.getText().toString()));
        startService(intent);
    }
}
