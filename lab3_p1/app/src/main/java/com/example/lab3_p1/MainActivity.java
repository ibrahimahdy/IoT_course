package com.example.lab3_p1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.util.Log;
public class MainActivity extends AppCompatActivity {

    LinearLayout theList;
;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String titles [] = getResources().getStringArray(R.array.recipe_titles);
        theList = findViewById(R.id.list_layout);

        for(int i = 0;i<titles.length; i++)
        {
            final int ind = i;
            Button listBtn = new Button(this);
            listBtn.setText(titles[i]);
            listBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    openDetailsActivity(titles[ind], ind);
                }
            });

            theList.addView(listBtn);
        }
        this.setContentView(theList);

    }
    static final int GO_TO_DETAILS = 1;
    static final int RATING = 1;

    public void openDetailsActivity(String title, int index){
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("theTitle", title);
        intent.putExtra("theIndex", index);
        startActivityForResult(intent, GO_TO_DETAILS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GO_TO_DETAILS) {
            if (resultCode == RATING) {

                int score = (int)data.getExtras().getFloat("theRating");
                int indexValue = data.getExtras().getInt("theIndex");

                // find resource id by using its name
                Log.i("scoreValue", ""+score);
                Log.i("indexValue", ""+indexValue);

                int color_id = getResources().getIdentifier("color_rating_" + score, "color", getPackageName());
                Log.i("color_id", ""+color_id);

                int color = ContextCompat.getColor(this, color_id);


                Button button = (Button)theList.getChildAt(indexValue);
                button.setBackgroundColor(color);
            }
        }
    }



}
