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
import android.widget.SearchView;
import android.widget.TextView;

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
    SearchView editsearch;
    CursorAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View theView = inflater.inflate(R.layout.fragment_list, container, false);
        theList = theView.findViewById(R.id.list_linearlayout);
        contacts_listview = theView.findViewById(R.id.contacts_listview);


        instance = MainActivity.getInstance();
        setAdapter(retrieveContacts());


        editsearch = (SearchView) theView.findViewById(R.id.search);
//        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
////                adapter.getFilter().filter(query);
////                adapter.notifyDataSetChanged();
//                return false;
//            }
//
//
//        });

        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("well", " this worked");
                return false;
            }
        });
        return theView;
    }


    private Cursor retrieveContacts(){
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

        return profileCursor;
    }


    private void setAdapter(Cursor profileCursor){

        if(profileCursor != null){
            Log.i("Done", "Rows: " + profileCursor.getCount());

            int emailSent = -1;
            Bundle extras =  this.getArguments();
            if(extras != null) {
                emailSent = extras.getInt("emailSent");
            }


            adapter = new ContactsCursorAdapter(instance,profileCursor,emailSent);
            contacts_listview.setAdapter(adapter);



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
                    int itemPosition = cursor.getPosition();

                    Log.i("clickable", name + "/" + phone + "/" + email);

                    MainActivity.getInstance().displayDetailsFragment(name, phone, email, itemPosition);
                }
            });
        }

    }




}
