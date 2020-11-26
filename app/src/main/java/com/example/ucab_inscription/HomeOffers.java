package com.example.ucab_inscription;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeOffers extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG="TAG";
    ListView simpleList;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_offers);
        simpleList = (ListView) findViewById(R.id.simpleListView);
       //  listView = (ListView)findViewById(R.id.listView);

// Fetch collaborators list from Firestore
        db.collection("offer_trip")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                AnOffer offer = new AnOffer(document.get("destination").toString(),document.get("departure").toString(), document.getId());

                                ArrayList<AnOffer> o= new ArrayList<>();
                                o.add(offer);
                                AnOffer[] offertable= new AnOffer[]{offer};
                               // ArrayAdapter<AnOffer> arrayAdapterr = new ArrayAdapter<AnOffer>();

                                ArrayAdapter<AnOffer> arrayAdapter = new ArrayAdapter<AnOffer>(HomeOffers.this, android.R.layout.simple_list_item_1, offertable);
                                listView.setAdapter(arrayAdapter);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

       /* ListView listView = (ListView)findViewById(R.id.list_offer);



        AnOffer tom = new Offer("Tom","admin");
        UserAccount jerry = new UserAccount("Jerry","user");
        UserAccount donald = new UserAccount("Donald","guest", false);

        UserAccount[] users = new UserAccount[]{tom,jerry, donald};


        // android.R.layout.simple_list_item_1 is a constant predefined layout of Android.
        // used to create a ListView with simple ListItem (Only one TextView).

        ArrayAdapter<UserAccount> arrayAdapter
                = new ArrayAdapter<UserAccount>(this, android.R.layout.simple_list_item_1 , users);

        listView.setAdapter(arrayAdapter);*/
    }
}