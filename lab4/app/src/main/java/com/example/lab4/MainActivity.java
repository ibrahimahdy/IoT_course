package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static MainActivity instance = new MainActivity();

    private final BroadcastReceiver myReceiver = new CallReceiver(instance);
    private ArrayList<String> phoneNumberList = new ArrayList<String>();
    private static ArrayAdapter<String> adapter;
    ListView numbers_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // register the broadcast
        IntentFilter filter = new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        this.registerReceiver(myReceiver, filter);

        phoneNumberList.add("hi");
        phoneNumberList.add("hi");
        phoneNumberList.add("hi");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, phoneNumberList);
        numbers_listview = (ListView)findViewById(R.id.numbers_listview);
        numbers_listview.setAdapter(adapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }


    public void addPhoneNumberToList(String phoneNumber){
        phoneNumberList.add("hi");
        adapter.notifyDataSetChanged();
    }
}
