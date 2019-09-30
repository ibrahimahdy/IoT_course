package com.example.lab4;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {

    MainActivity mainActivity;
    public CallReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
    public MainActivity getMainActivity(){
        return mainActivity;
    }
    public void setMainActivity(MainActivity activity) {
        this.mainActivity = activity;
    }

    private static final String TAG = "MyBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null){
            String action = intent.getAction();
            Log.i(TAG, action);


            if (intent.hasExtra(TelephonyManager.EXTRA_STATE)){
                String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                Log.i(TAG, state);
            }

            if (intent.hasExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)){
                String phoneNo = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.i(TAG, phoneNo);

                getMainActivity().addPhoneNumberToList(phoneNo);
            }
        }
    }
}