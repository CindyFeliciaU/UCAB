package com.example.ucab_inscription;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Offer_details extends AppCompatActivity {
    private static final String TAG ="TAG" ;
    TextView textTitle;
    FirebaseFirestore fStore;
    TextView list_seats;
    TextView list_hour;
    TextView list_date;
    TextView list_price;
    TextView list_destination;
    TextView list_departure;
    TextView driverName;
    TextView driverInfo;
    ImageView profilPic;
    FirebaseAuth fAuth;
    FirebaseUser offer;

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);
        driverInfo=findViewById(R.id.driverInfo);
        driverName=findViewById(R.id.driverName);
        list_departure=findViewById(R.id.list_departure);
        list_destination=findViewById(R.id.list_destination);
        list_hour= findViewById(R.id.list_hour);
        list_seats=findViewById(R.id.list_seats);
        list_date= findViewById(R.id.list_date);
        list_price=findViewById(R.id.list_price);
        profilPic=findViewById(R.id.profilPic);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        offer = fAuth.getCurrentUser();

        String ItemId = getIntent().getStringExtra("TripId");
        Toast.makeText(Offer_details.this, ItemId, Toast.LENGTH_SHORT).show();

        fStore = FirebaseFirestore.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();

        DocumentReference docRef = fStore.collection("offer_trip").document(ItemId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {

                        DocumentReference documentReference = fStore.collection("users").document(document.getString("userId"));
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                driverName.setText(documentSnapshot.getString("fName "));
                            }
                        });
                        StorageReference profileRef =storageReference.child("users/"+document.getString("userId")+"/profile.jpg");
                        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
                            @Override
                            public void onSuccess(Uri uri){
                                Picasso.get().load(uri).into(profilPic);
                            }
                        });
                        if(!document.getString("comment").isEmpty()) {
                            driverInfo.setText(document.getString("comment"));
                        }
                        list_hour.setText(document.getString("hour"));
                        list_price.setText(document.getString("price"));
                        list_seats.setText(document.getString("seats"));
                        list_date.setText(document.getString("date"));
                        list_departure.setText(document.getString("departure "));
                        list_destination.setText(document.getString("destination "));
                        findViewById(R.id.Book).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getApplicationContext(),Book.class);
                                startActivity(i);
                            }
                        });

                    } else {
                        Log.d(TAG, "No such offer");
                        Intent intent = new Intent(getApplicationContext(), OfferListAdapter.class);
                        startActivity(intent);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            textTitle = findViewById(R.id.list_departure);
            Intent i = getIntent();
            String title = i.getStringExtra("title");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(getApplicationContext(), ProfilActivity.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.item2:
                Intent intent2 = new Intent(getApplicationContext(), OfferListAdapter.class);
                startActivity(intent2);
                finish();
                return true;

            case R.id.item3:
                Intent intent3 = new Intent(getApplicationContext(), TripOffer.class);
                startActivity(intent3);
                finish();
                return true;
            case R.id.item4:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}