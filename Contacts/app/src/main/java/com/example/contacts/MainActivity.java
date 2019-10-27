package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;


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
            displayListFragment(-1);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    displayListFragment(-1);
                }
                return;
            }
        }
    }
    ListFragment listFragment;

    public void displayListFragment(int emailSent) {
        listFragment = new ListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, listFragment, "listFragmentTag").commit();

        Bundle arguments = new Bundle();
        arguments.putInt("emailSent", emailSent);
        listFragment.setArguments(arguments);

    }


    public void displayDetailsFragment(String name, String phone, String email, int itemPosition) {

        DetailsFragment detailsFragment = new DetailsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, detailsFragment, "detailsFragmentTag").addToBackStack(null).commit();

        Bundle arguments = new Bundle();
        arguments.putString("theName", name);
        arguments.putString("thePhone", phone);
        arguments.putString("theEmail", email);
        arguments.putInt("itemPosition", itemPosition);
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
