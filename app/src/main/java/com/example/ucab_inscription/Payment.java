package com.example.ucab_inscription;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Payment extends AppCompatActivity {
/*
    Button payment;
    EditText bankName;
    EditText nameHolder;
    EditText accountNumber;
    EditText swift;
    FirebaseDatabase fDatabase;
    FirebaseUser user;
    String userId;

    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        userId=user.getUid();

        setContentView(R.layout.activity_payment);
        bankName= findViewById(R.id.editTextBankName);
        nameHolder = findViewById(R.id.editTextAccountHolderName);
        accountNumber = findViewById(R.id.editTextAccountNumber);
        swift = findViewById(R.id.editTextSwiftCode);
        payment = findViewById(R.id.buttonPayment);


        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String bank = bankName.getText().toString().trim();
                final String name = nameHolder.getText().toString().trim();
                final String account = accountNumber.getText().toString().trim();
                final String swiftCode = swift.getText().toString().trim();

                if (TextUtils.isEmpty(bank)) {
                    bankName.setError("Your bank name is required");
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    nameHolder.setError("Your name is required");
                    return;
                }

                if (TextUtils.isEmpty(account) && account.length()!=13) {
                    accountNumber.setError("Your account number must have the 13 characters required");
                    return;
                }
                if (TextUtils.isEmpty(swiftCode)) {
                    swift.setError("Your swift code is required");
                    return;
                }

                Map<String, Object> payment = new HashMap<>();
                 payment.put("userId", userId);
            /*     payment.put("departure", sdepart);
                trip_offer.put("destination", desti);
                trip_offer.put("date", date);
                trip_offer.put("time", time);
                trip_offer.put("seats", seats);
                trip_offer.put("price", price);
                trip_offer.put("comment", comment);
                fStore.collection("offer_trip")
                        .add(trip_offer)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "Trip offer added with ID: " + documentReference.getId());
                                startActivity(new Intent(getApplicationContext(), OfferListAdapter.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding the offer", e);
                            }
                        });
            }*//*


        //});





    }


*/



}