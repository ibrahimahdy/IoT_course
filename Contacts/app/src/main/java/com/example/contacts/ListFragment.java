package com.example.contacts;


import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.FilterQueryProvider;
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
    CursorAdapter adapter;


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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    private void retrieveContacts(){
        final String projection [] = new String[]
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

            int emailSent = -1;
            Bundle extras =  this.getArguments();
            if(extras != null) {
                emailSent = extras.getInt("emailSent");
            }


            adapter = new ContactsCursorAdapter(instance,profileCursor,emailSent);
            contacts_listview.setAdapter(adapter);

            adapter.setFilterQueryProvider(new FilterQueryProvider() {

                public Cursor runQuery(CharSequence constraint) {
                    Log.d("theFilter", "runQuery constraint:"+constraint);

                    Cursor filteredCursor =
                            instance.getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    projection,
                                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME +  " Like '%" + constraint + "%'",
                                    null,
                                    null);
                    return filteredCursor; //now your adapter will have the new filtered content
                }

            });

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

                    Log.i("clickable", name + "/" + phone + "/" + email + "/" + position);

                    MainActivity.getInstance().displayDetailsFragment(name, phone, email, position);
                }
            });
        }

    }



    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.options_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);
                    adapter.getFilter().filter(newText);
                    adapter.notifyDataSetChanged();
                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);
                    adapter.getFilter().filter(query);
                    adapter.notifyDataSetChanged();
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }


}
