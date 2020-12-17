package com.example.ucab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MyOffer_details extends AppCompatActivity {
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
    String userId;
    StorageReference storageReference;
    String ItemId;
    String customerName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_offer_details);
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

        ItemId = getIntent().getStringExtra("ItemId");
        fStore = FirebaseFirestore.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();

        DocumentReference docRef = fStore.collection("offer_trip").document(ItemId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        userId=document.getString("userId");
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
                        list_hour.setText(document.getString("time"));
                        list_price.setText(document.getString("price"));
                        list_seats.setText(document.getString("seats"));
                        list_date.setText(document.getString("date"));
                        list_departure.setText(document.getString("departure"));
                        list_destination.setText(document.getString("destination"));
                        findViewById(R.id.Edit).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getApplicationContext(), MyTripOffer.class);
                                i.putExtra("ItemId", ItemId);
                                i.putExtra("driverName", driverName.getText().toString());
                                i.putExtra("list_hour", list_hour.getText().toString());
                                i.putExtra("list_price", list_price.getText().toString());
                                i.putExtra("list_seats",list_seats.getText().toString());
                                i.putExtra("list_date", list_date.getText().toString());
                                i.putExtra("list_departure", list_departure.getText().toString());
                                i.putExtra("list_destination",list_destination.getText().toString());
                                 startActivity(i);
                            }
                        });

                        findViewById(R.id.Delete).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                        fStore.collection("offer_trip").document(ItemId).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Log.d(TAG, "Your offer successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error deleting the offer", e);
                                            }
                                        });

                                Intent i = new Intent(getApplicationContext(), HistoryOffers.class);
                                startActivity(i);
                                Toast.makeText(MyOffer_details.this, "Your offer successfully deleted!", Toast.LENGTH_SHORT).show();

                            }
                        });

                    } else {
                        Log.d(TAG, "No such offer");
                        Intent intent = new Intent(getApplicationContext(), HistoryOffers.class);
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

            case R.id.item5:
                Intent intent4 = new Intent(getApplicationContext(), HistoryOffers.class);
                startActivity(intent4);
                finish();
                return true;

            case R.id.item6:
                Intent intent6 = new Intent(getApplicationContext(), CustomerReceipts.class);
                startActivity(intent6);
                finish();
                return true;

            case R.id.item7:
                Intent intent7 = new Intent(getApplicationContext(), DriverReceipts.class);
                startActivity(intent7);
                finish();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}