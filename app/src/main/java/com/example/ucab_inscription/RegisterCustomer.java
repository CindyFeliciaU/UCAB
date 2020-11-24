package com.example.ucab_inscription;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RegisterCustomer extends AppCompatActivity {
 /*  EditText mFullName, mEmail, mPassword, mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userId;
    private String TAG="TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        mPhone = findViewById(R.id.phone);
        mRegisterBtn= findViewById(R.id.registerBtn);
        mLoginBtn= findViewById(R.id.loginBtn);
        fAuth=FirebaseAuth.getInstance();
        progressBar= findViewById(R.id.progressBar);
        fStore = FirebaseFirestore.getInstance();
        //Check if the user is already connected or has already an account
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        //Register the user into the Firebase

        mRegisterBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String fullName=mFullName.getText().toString().trim();
                final String phone = mPhone.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required");
                    return;
                }

                if(password.length()<6){
                    mPassword.setError("Password mist be >= 6 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //If the user is created sucessfully
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "User created",  Toast.LENGTH_SHORT).show();
                            //Store data
                            userId=fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userId);
                            Map<String, Object> user = new HashMap<>();
                            user.put("fName ",fullName);
                            user.put("email ",email);
                            user.put("phone", phone);
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
                            //Send the user to the MainActivity
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else{
                            Toast.makeText(Register.this, "Error :"+task.getException().getMessage(),  Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }*/
}