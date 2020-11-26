package com.example.ucab_inscription;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    Button profil;
    Button logout;
    Button tripoffer;
    Button listoffer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tripoffer = findViewById(R.id.tripoffer);
        listoffer= findViewById(R.id.offerlist);

        listoffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, OfferListAdapter.class);
                startActivity(intent);
                finish();
                return;
            }
        });
        tripoffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, TripOffer.class);
                startActivity(intent);
                finish();
                return;
            }
        });
        profil = findViewById(R.id.profil);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, ProfilActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });


    }
    public void lougout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}