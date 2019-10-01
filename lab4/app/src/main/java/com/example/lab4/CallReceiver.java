package com.example.lab4;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {

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
                Log.i(TAG, TelephonyManager.EXTRA_STATE_RINGING);
                if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)){
                    MainActivity.getInstance().addPhoneNumberToList(phoneNo);
                }
            }
        }
    }
}