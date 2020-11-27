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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.DatabaseId;

import java.util.HashMap;
import java.util.Map;

public class CustomerLoginActivity extends AppCompatActivity {

    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mCreateBtn;
    Button mRegistration;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener fireBaseAuthListener;
    Boolean registration=false;
    Intent intent;
    String userId;
    FirebaseFirestore mStore;
    private String TAG="TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.loginBtn);


        mStore = FirebaseFirestore.getInstance();
        mRegistration = findViewById(R.id.registrationBtn);
        mCreateBtn = findViewById(R.id.registerBtn);
        fireBaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Check for the user status
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {


                    intent = new Intent(CustomerLoginActivity.this, HomeCustomer.class);
                    //}
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        mRegistration.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("Password mist be >= 6 characters");
                    return;
                }

                // progressBar.setVisibility(View.VISIBLE);


                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CustomerLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //If the user is created sucessfully
                        if (task.isSuccessful()) {
                            Toast.makeText(CustomerLoginActivity.this, "Register in successfully", Toast.LENGTH_SHORT).show();
                            //Get the user id from the firebase
                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user_id);
                            current_user_db.setValue(true);
                            registration=true;

                            userId=mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = mStore.collection("users").document(userId);
                            Map<String, Object> user = new HashMap<>();
                            // user.put("fName ",fullName);
                            user.put("email ",email);
                            // user.put("phone", phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d( TAG, "onSuccess: user Profile created for "+ userId);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: "+e.toString());
                                }
                            });

                        } else {
                            Toast.makeText(CustomerLoginActivity.this, "Sign up error :" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

  /*      mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
*/
        mLoginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("Password mist be >= 6 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(CustomerLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //If the user is created sucessfully
                        if (task.isSuccessful()) {
                            Toast.makeText(CustomerLoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            //Get the user id from the firebase
                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers");
                            current_user_db.setValue(true);
                        } else {
                            Toast.makeText(CustomerLoginActivity.this, "Errorr :" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(fireBaseAuthListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.addAuthStateListener(fireBaseAuthListener);
    }
}