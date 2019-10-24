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

    ListView contacts_listview;
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
                        ContactsContract.CommonDataKinds.Phone._ID,
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

        if(profileCursor != null){
            Log.i("Done", "Rows: " + profileCursor.getCount());

            contacts_listview = (ListView)findViewById(R.id.contacts_listview);
            contacts_listview.setAdapter(new ContactsCursorAdapter(this,profileCursor));

            contacts_listview.setClickable(true);
            contacts_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i("clickable", "I am clicked");

                    Cursor cursor = ((CursorAdapter)contacts_listview.getAdapter()).getCursor();
                    cursor.moveToPosition(position);

                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String email = getEmail(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)));
                    Log.i("clickable", name + "/" + phone + "/" + email);


                    openDetailsActivity(name, phone, email);

                }
            });


        }


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


    static final int GO_TO_DETAILS = 1;

    public void openDetailsActivity(String name, String phone, String email){
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("theName", name);
        intent.putExtra("thePhone", phone);
        intent.putExtra("theEmail", email);
        startActivityForResult(intent, GO_TO_DETAILS);
    }

}
