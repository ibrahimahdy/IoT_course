package com.example.lab3_p2;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {


    public DetailsFragment() {
        // Required empty public constructor
    }
    TextView theTitle;
    TextView theDescription;

    int indexValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View theView = inflater.inflate(R.layout.fragment_details, container, false);;

        Bundle bundle =  this.getArguments();
        String title = bundle.getString("title");
        int index = bundle.getInt("index");

        final String descValues [] = getResources().getStringArray(R.array.recipe_values);

        theTitle = (TextView)theView.findViewById(R.id.titleView);
        theTitle.setText(title);

        theDescription = (TextView)theView.findViewById(R.id.discView);
        theDescription.setText(descValues[index]);

        Button button = (Button)theView.findViewById(R.id.closebtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                closeDetails();
            }
        });

        return theView;
    }


    public void closeDetails() {
        ((MainActivity)getActivity()).displayListFragment();
    }
}
