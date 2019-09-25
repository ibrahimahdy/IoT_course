package com.example.lab3_p1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    TextView title;
    TextView description;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final String descValues [] = getResources().getStringArray(R.array.recipe_values);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            title = (TextView)findViewById(R.id.titleView);
            title.setText(extras.getString("theTitle"));

            description = (TextView)findViewById(R.id.descView);
            description.setText(descValues[extras.getInt("theindex")]);
        }

    }
}
