package com.example.ucab_inscription;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilActivity extends AppCompatActivity {

    private static final String USER = "users";
    private TextView occupationTextView, nameTextView, workTextView;
    private TextView emailTextView, phoneTextView;
    private ImageView emailImageView, phoneImageView;
    private String email, password;
    TextView myTextView;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    Intent intent = getIntent();
    String mail = intent.getStringExtra("email");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        phoneTextView = findViewById(R.id.phoneTextView);

        //Get the user
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(USER);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                //    if()
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}