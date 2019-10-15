package com.example.lab6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText count;
    GridView myView;
    List<Bitmap> imageList = new ArrayList<>();

    private final BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            byte[] byteArray = intent.getByteArrayExtra("result");
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageList.add(bmp);
            myView.setAdapter(new CatAdapter(imageList));
            Log.i("done", ""+ byteArray.length);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myView = (GridView) findViewById(R.id.cat_gridview);

        IntentFilter filter = new IntentFilter();
        filter.addAction("downloadedFiles");
        this.registerReceiver(myReceiver, filter);


        Button buttonOne = (Button) findViewById(R.id.button);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                get_cats();
            }
        });
    }

    public void get_cats(){

        Log.i("getCats", "button clicked");
        count = (EditText)findViewById(R.id.images_count);

        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra("imgCount", Integer.parseInt(count.getText().toString()));
        startService(intent);
    }
}
