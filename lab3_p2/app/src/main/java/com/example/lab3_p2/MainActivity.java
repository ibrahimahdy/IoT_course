package com.example.lab3_p2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayListFragment();
    }


    public void displayListFragment() {
        ListFragment listFragment = new ListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, listFragment, "listFragmentTag").commit();
    }


    public void displayDetailsFragment(String title, int index) {

        DetailsFragment detailsFragment = new DetailsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, detailsFragment, "detailsFragmentTag").commit();

        Bundle arguments = new Bundle();
        arguments.putString("title", title);
        arguments.putInt("index", index);
        detailsFragment.setArguments(arguments);
    }
}
