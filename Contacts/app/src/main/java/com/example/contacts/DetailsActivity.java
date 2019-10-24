package com.example.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    TextView name;
    TextView phone;
    TextView email;
    Button sendButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            name = (TextView)findViewById(R.id.name);
            name.setText(extras.getString("theName"));

            phone = (TextView)findViewById(R.id.phone);
            phone.setText(extras.getString("thePhone"));

            email = (TextView)findViewById(R.id.email);
            final String contactEmail= extras.getString("theEmail");
            email.setText(contactEmail);

            sendButton = (Button) findViewById(R.id.send_email);
            sendButton.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    sedEmail(contactEmail);
                }
            });

        }

    }



    public void sedEmail(String email){

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");

        i.putExtra(Intent.EXTRA_EMAIL,new String[]{email});

        String emailTitle = ((EditText)findViewById(R.id.title)).getText().toString();
        i.putExtra(Intent.EXTRA_SUBJECT, emailTitle);

        String emailBody = ((EditText)findViewById(R.id.message)).getText().toString();
        i.putExtra(Intent.EXTRA_TEXT, emailBody);
        
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
           Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
