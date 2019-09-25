package com.example.lab3_p1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    TextView title;
    TextView description;

    int indexValue;


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

            indexValue = extras.getInt("theIndex");
            description = (TextView)findViewById(R.id.descView);
            description.setText(descValues[indexValue]);

        }

    }


    public final int RATING = 1;

    public void saveRating(View v){

        RatingBar mBar = (RatingBar) findViewById(R.id.myRating);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("theRating", mBar.getRating());

        Log.i("theRating", ""+mBar.getRating());

        resultIntent.putExtra("theIndex", indexValue);

        Log.i("theIndex", ""+indexValue);


        setResult(RATING, resultIntent);
        finish();


    }
}
