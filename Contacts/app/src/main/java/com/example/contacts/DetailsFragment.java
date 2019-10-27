package com.example.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {


    public DetailsFragment() {
        // Required empty public constructor
    }

    TextView name;
    TextView phone;
    TextView email;
    Button sendButton;


    private int pos;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View theView = inflater.inflate(R.layout.fragment_details, container, false);;

        Bundle extras =  this.getArguments();
        if(extras != null){
            name = (TextView)theView.findViewById(R.id.name);
            name.setText(extras.getString("theName"));

            phone = (TextView)theView.findViewById(R.id.phone);
            phone.setText(extras.getString("thePhone"));

            email = (TextView)theView.findViewById(R.id.email);
            final String contactEmail= extras.getString("theEmail");
            email.setText(contactEmail);
            pos = extras.getInt("itemPosition");

            sendButton = (Button)theView.findViewById(R.id.send_email);
            sendButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    sedEmail(theView, contactEmail);
                }
            });

        }

        return theView;
    }




    public void sedEmail(View theView, String email){

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");

        i.putExtra(Intent.EXTRA_EMAIL,new String[]{email});

        String emailTitle = ((EditText)theView.findViewById(R.id.title)).getText().toString();
        i.putExtra(Intent.EXTRA_SUBJECT, emailTitle);

        String emailBody = ((EditText)theView.findViewById(R.id.message)).getText().toString();
        i.putExtra(Intent.EXTRA_TEXT, emailBody);

        try {
            startActivityForResult(i, 1);

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.getInstance(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
               Fragment listFragment =  fm.findFragmentByTag("listFragmentTag");
                Bundle extras =  listFragment.getArguments();
                if(extras != null) {
                    extras.putInt("emailSent", pos);
                    listFragment.setArguments(extras);
                }
                fm.popBackStack();
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");
            }
        }
    }

}
