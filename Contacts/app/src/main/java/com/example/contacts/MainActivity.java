package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    HashMap<String, String> myContact;
    ArrayList<HashMap<String,String>> contactsList = new ArrayList();
    ListView contacts_listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String requiredPermissions[] = {Manifest.permission.READ_CONTACTS};
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,requiredPermissions,  1);
        }
        else{
            retrieveContacts();
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    retrieveContacts();
                }
                return;
            }
        }
    }


    private void retrieveContacts(){

        String projection [] = new String[]
                {
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };

        Cursor profileCursor =
                getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        projection ,
                        null,
                        null,
                        null);


        String name="",phoneNumber="",emailAddress="";
        if(profileCursor != null){
            Log.i("Done", "Rows: " + profileCursor.getCount());


            while(profileCursor.moveToNext()){
                name = profileCursor.getString(profileCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                phoneNumber = profileCursor.getString(profileCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                String contactId = profileCursor.getString(profileCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,null, null);
                if (emails.moveToFirst())
                {
                    emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                }
                emails.close();

                myContact = new HashMap<String, String>();
                myContact.put("name", name);
                myContact.put("phoneNumber", phoneNumber);
                myContact.put("emailAddress", emailAddress);
                contactsList.add(myContact);

            }

            contacts_listview = (ListView)findViewById(R.id.contacts_listview);

            contacts_listview.setAdapter(new CustomAdapter(contactsList));
        }

    }

}
