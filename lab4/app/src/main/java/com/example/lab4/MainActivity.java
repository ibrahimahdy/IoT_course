package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;

    private final BroadcastReceiver myReceiver = new CallReceiver();
    private ArrayList<String> phoneNumberList = new ArrayList<String>();
    private static ArrayAdapter<String> adapter;
    ListView numbers_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;


        String requiredPermissions[] = new String[2];
        requiredPermissions[0] = Manifest.permission.READ_PHONE_STATE;
        requiredPermissions[1] = Manifest.permission.READ_CALL_LOG;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {

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

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    private void registerReceiver(){
        IntentFilter filter = new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        this.registerReceiver(myReceiver, filter);

        //  adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, phoneNumberList);
        numbers_listview = (ListView)findViewById(R.id.numbers_listview);
        //  numbers_listview.setAdapter(adapter);

        numbers_listview.setAdapter(new CustomAdapter(phoneNumberList));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    public static MainActivity getInstance() {
        return instance;
    }
    public void addPhoneNumberToList(String phoneNumber){
        phoneNumberList.add(phoneNumber);

        //adapter.notifyDataSetChanged();
        numbers_listview.setAdapter(new CustomAdapter(phoneNumberList));
    }

    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
