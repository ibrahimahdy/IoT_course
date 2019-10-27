package com.example.contacts;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ContactsCursorAdapter extends CursorAdapter {

    private int emailSent;

    public ContactsCursorAdapter(Context context, Cursor cursor, int emailSent) {
        super(context, cursor, 0);
        this.emailSent = emailSent;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.custom_list_item, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView name;
        final String theName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        name = (TextView) view.findViewById(R.id.name);
        name.setText(theName);


        TextView phone;
        final String theNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        phone = (TextView) view.findViewById(R.id.phoneNumber);
        phone.setText(theNumber);


        String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
        TextView email;
        final String theEmail = MainActivity.getInstance().getEmail(contactId);
        email = (TextView) view.findViewById(R.id.emailAddress);
        email.setText(theEmail);

        TextView sent;
        sent = (TextView) view.findViewById(R.id.emailSent);
        sent.setText("");

        if(emailSent !=-1 && emailSent == cursor.getPosition()){
            sent.setText("E-mail was composed!");
        }
    }


}
