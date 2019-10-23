package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ListView;
import android.provider.ContactsContract;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String requiredPermissions[] = {Manifest.permission.READ_CONTACTS};
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,requiredPermissions,  1);
        }
        else{
            registerReceiver();
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    registerReceiver();
                }
                return;
            }
        }
    }


    private void registerReceiver(){
        //Log.i("permission", "Granted!");
        // Sets the columns to retrieve for the user profile
        String projection [] = new String[]
                {
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };

        // Retrieves the profile from the Contacts Provider
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


                Log.i("Done", "Contact: " + name + " -- " + phoneNumber+ " -- " + emailAddress);

            }
        }

    }

}
