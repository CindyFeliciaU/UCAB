package com.example.ucab;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Book extends AppCompatActivity {
    String TAG;
    CardForm cardForm;
    Button buy;
    FirebaseFirestore fStore;
    AlertDialog.Builder alertBuilder;
    String userId;
    FirebaseAuth fAuth;
    String ItemId;
    String customerName;
    String driverName;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        user = FirebaseAuth.getInstance().getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        cardForm = findViewById(R.id.card_form);
        buy = findViewById(R.id.btnBuy);
        this.ItemId = getIntent().getStringExtra("ItemId");
        this.driverName = getIntent().getStringExtra("driverName");

        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .setup(Book.this);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardForm.isValid()) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Book.this);
                    alertBuilder.setTitle("Confirm before purchase");
                    alertBuilder.setMessage("Card number: " + cardForm.getCardNumber() + "\n" +
                            "Card expiry date: " + cardForm.getExpirationMonth() + cardForm.getExpirationYear() + "\n" +
                            "Card CVV: " + cardForm.getCvv() + "\n" +
                            "Postal code: " + cardForm.getPostalCode() + "\n" +
                            "Phone number: " + cardForm.getMobileNumber());
                    alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            final String price = getIntent().getStringExtra("price");


                            final DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(userId);
                            final Long[] oldScore = new Long[1];

                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()){
                                        oldScore[0] = (Long) documentSnapshot.get("score");
                                        Long new_score = oldScore[0] + Long.parseLong(price);
                                        Map<String, Long> map = new HashMap<>();
                                        map.put("score", new_score);
                                        docRef.set(map, SetOptions.merge());
                                    }
                                }
                            });
                            dialogInterface.dismiss();
                            Toast.makeText(Book.this, "Thank you for purchase", Toast.LENGTH_LONG).show();
                            updateO();
                            Intent in = new Intent(getApplicationContext(), OfferListAdapter.class);
                            startActivity(in);



                        }
                    });
                    alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();


                } else {
                    Toast.makeText(Book.this, "Please complete the form", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void updateO(){

        DocumentReference documentReference = fStore.collection("users").document(user.getUid());

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                customerName= (value.getString("fName "));
            }
            });
        final DocumentReference docRef = fStore.collection("offer_trip").document(ItemId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map<String, Object> trip_payment = new HashMap<>();
                    trip_payment.put("customerName", customerName);
                    trip_payment.put("driverName", driverName);
                    trip_payment.put("customerId", userId);
                    trip_payment.put("TripId", (ItemId));
                    trip_payment.put("time", document.getString("time"));
                    trip_payment.put("departure", document.getString("departure"));
                    trip_payment.put("destination", document.getString("destination"));
                    trip_payment.put("driverId", document.getString("userId"));
                    trip_payment.put("date", document.getString("date"));
                    trip_payment.put("price", document.getString("price"));
                    String d = document.getString("seats");
                    int e = Integer.parseInt(d);

                    if (e - 1 > 0) {
                        String seats = String.valueOf(e-1);
                        //diminuer
                        docRef.update("seats", seats).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });


                    }
                    else {
                        //delete
                        FirebaseFirestore.getInstance().collection("offer_trip").document(ItemId)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                    }

                    fStore.collection("trip_payment")
                            .add(trip_payment)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    startActivity(new Intent(getApplicationContext(), OfferListAdapter.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }
            }
        });
    }
}
