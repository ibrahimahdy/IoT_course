package com.example.contacts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class CustomAdapter extends BaseAdapter {

    List<HashMap<String,String>> objects;

    public CustomAdapter(List<HashMap<String,String>> numbers) {
        objects = numbers;
    }


    public HashMap<String,String> getItem(int pos) {
        return objects.get(pos);
    }


    public int getCount() {
        return objects.size();
    }


    public long getItemId(int position) {
        return objects.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        //final int thePos = position;
        View view;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            view = layoutInflater.inflate(R.layout.custom_list_item, parent, false);


            TextView name;
            final String theName = getItem(position).get("name");
            name = (TextView) view.findViewById(R.id.name);
            name.setText(theName);


            TextView phone;
            final String theNumber = getItem(position).get("phoneNumber");
            phone = (TextView) view.findViewById(R.id.phoneNumber);
            phone.setText(theNumber);


            TextView email;
            final String theEmail = getItem(position).get("emailAddress");
            email = (TextView) view.findViewById(R.id.emailAddress);
            email.setText(theEmail);

        } else{
            view = convertView;
        }

        return (view);
    }


}
