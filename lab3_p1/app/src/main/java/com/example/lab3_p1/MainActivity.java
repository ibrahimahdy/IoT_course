package com.example.lab3_p1;

import androidx.appcompat.app.AppCompatActivity;

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


    public void openDetailsActivity(String title, int index){
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("theTitle", title);
        intent.putExtra("theIndex", index);
        startActivity(intent);
    }

}
