package com.example.lab6;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class CatAdapter extends BaseAdapter {

    List<Bitmap> imageList;

    public CatAdapter(List<Bitmap> images) {
        imageList = images;
    }

    public Bitmap getItem(int pos) {
        return imageList.get(pos);
    }


    public int getCount() {
        return imageList.size();
    }


    public long getItemId(int position) {
        return imageList.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView = new ImageView(parent.getContext());
        if (convertView == null) {
            imageView.setImageBitmap(getItem(position));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setLayoutParams(new AbsListView.LayoutParams(600,600));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(6, 6, 6 ,6);

        } else{
            imageView = (ImageView)convertView;
        }
        return (imageView);
    }
}


