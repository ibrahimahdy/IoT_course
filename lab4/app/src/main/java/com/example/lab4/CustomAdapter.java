package com.example.lab4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends BaseAdapter {

    List<String> objects;

    public CustomAdapter(List<String> numbers) {
        objects = numbers;
    }


    public String getItem(int pos) {
        return objects.get(pos);
    }


    public int getCount() {
        return objects.size();
    }


    public long getItemId(int position) {
        return objects.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            view = layoutInflater.inflate(R.layout.custom_list_item, parent, false);

            TextView phone;
            phone = (TextView) view.findViewById(R.id.phoneNumber);
            phone.setText(getItem(position));

        } else{
            view = convertView;
        }

        return (view);
    }
}
