package com.example.lab5firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //addData(db);
        getData(db);

    }

    private void addData(FirebaseFirestore db){

        Map<String, Object> city = new HashMap<>();
        city.put("name", "Tallinn");
        city.put("population", 434562);
        city.put("area_km2", 159);
        insdetData(db, city, "TLL");

        city = new HashMap<>();
        city.put("name", "Tartu");
        city.put("population", 93865);
        city.put("area_km2", 39);
        insdetData(db, city, "TRT");

        city = new HashMap<>();
        city.put("name", "Narva");
        city.put("population", 55249);
        city.put("area_km2", 68);
        insdetData(db, city, "NRV");
    }
    private void insdetData(FirebaseFirestore db, Map<String, Object> city, String doc){

        db.collection("cities").document(doc)
                .set(city)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("DocumentSnapshotSuccess", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DocumentSnapshotFail", "Error writing document", e);
                    }
                });
    }


    private void getData(FirebaseFirestore db){
        db.collection("cities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList myCities = new ArrayList();

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                myCities.add(document.getData());
                            }
                            Log.d("TheData-beforeSort",  " => " + myCities);

                            Collections.sort(myCities, new Comparator<HashMap<String, Object>>(){
                                public int compare(HashMap<String, Object> one, HashMap<String, Object> two) {
                                    Long codeOne = (Long)one.get("area_km2");
                                    Long codeTwo = (Long)two.get("area_km2");
                                    return codeTwo.compareTo(codeOne);
                                }
                            });
                            Log.d("TheData-afterSort",  " => " + myCities);
                        } else {
                            Log.d("TheDataFailed", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
