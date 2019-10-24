package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {


    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

        String requiredPermissions[] = {Manifest.permission.READ_CONTACTS};
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,requiredPermissions,  1);
        }
        else{
            displayListFragment();
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    displayListFragment();
                }
                return;
            }
        }
    }


    public void displayListFragment() {
        ListFragment listFragment = new ListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, listFragment, "listFragmentTag").commit();
    }


    public void displayDetailsFragment(String name, String phone, String email) {

        DetailsFragment detailsFragment = new DetailsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, detailsFragment, "detailsFragmentTag").commit();


        Bundle arguments = new Bundle();
        arguments.putString("theName", name);
        arguments.putString("thePhone", phone);
        arguments.putString("theEmail", email);
        detailsFragment.setArguments(arguments);
    }


    public static MainActivity getInstance() {
        return instance;
    }

    public String getEmail(String contactID) {
        String emailAddress="";
        Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactID,null, null);
        if (emails.moveToFirst())
        {
            emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
        }
        emails.close();
        return emailAddress;
    }

}
