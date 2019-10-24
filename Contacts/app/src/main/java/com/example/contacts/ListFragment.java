package com.example.contacts;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {


    public ListFragment() {
        // Required empty public constructor
    }

    private static Context instance;

    LinearLayout theList;
    ListView contacts_listview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View theView = inflater.inflate(R.layout.fragment_list, container, false);
        theList = theView.findViewById(R.id.list_linearlayout);
        contacts_listview = theView.findViewById(R.id.contacts_listview);

        instance = MainActivity.getInstance();
        retrieveContacts();
        return theView;
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
                instance.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        projection ,
                        null,
                        null,
                        null);

        if(profileCursor != null){
            Log.i("Done", "Rows: " + profileCursor.getCount());


            contacts_listview.setAdapter(new ContactsCursorAdapter(instance,profileCursor));
            contacts_listview.setClickable(true);
            contacts_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i("clickable", "I am clicked");

                    Cursor cursor = ((CursorAdapter)contacts_listview.getAdapter()).getCursor();
                    cursor.moveToPosition(position);

                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String email = MainActivity.getInstance().getEmail(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)));
                    Log.i("clickable", name + "/" + phone + "/" + email);

                    MainActivity.getInstance().displayDetailsFragment(name, phone, email);
                }
            });


        }


    }




}
