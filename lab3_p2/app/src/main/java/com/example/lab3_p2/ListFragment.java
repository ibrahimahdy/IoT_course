package com.example.lab3_p2;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {


    public ListFragment() {
        // Required empty public constructor
    }

    LinearLayout theList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View theView = inflater.inflate(R.layout.fragment_list, container, false);
        theList = theView.findViewById(R.id.list_linearlayout);

        final String titles [] = getResources().getStringArray(R.array.recipe_titles);
        for(int i = 0;i<titles.length; i++)
        {
            final int ind = i;
            Button listBtn = new Button(theView.getContext());
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


        return theView;

    }

    public void openDetailsActivity(String title, int index){
        ((MainActivity)getActivity()).displayDetailsFragment(title, index);
    }
}
