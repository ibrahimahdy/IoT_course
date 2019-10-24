package com.example.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    TextView name;
    TextView phone;
    TextView email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            name = (TextView)findViewById(R.id.name);
            name.setText(extras.getString("theName"));

            phone = (TextView)findViewById(R.id.phone);
            phone.setText(extras.getString("thePhone"));

            email = (TextView)findViewById(R.id.email);
            email.setText(extras.getString("theEmail"));

        }

    }


//    public final int RATING = 1;
//
//    public void saveRating(View v){
//
//        RatingBar mBar = (RatingBar) findViewById(R.id.myRating);
//        Intent resultIntent = new Intent();
//        resultIntent.putExtra("theRating", mBar.getRating());
//        resultIntent.putExtra("theIndex", indexValue);
//        setResult(RATING, resultIntent);
//        finish();
//    }
}
