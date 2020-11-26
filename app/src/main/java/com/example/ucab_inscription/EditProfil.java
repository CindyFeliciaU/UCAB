package com.example.ucab_inscription;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProfil extends AppCompatActivity {

    private static final String TAG ="TAG" ;
    EditText profilFullName,profilEmail, profilPhone;
    String userId;
    FirebaseFirestore fStore;
    Button saveBtn;
    FirebaseAuth fAuth;
    FirebaseUser user;
    ImageView profilPic;
    Button previous;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        Intent data = getIntent();
        final String fullName = data.getStringExtra("fullName");
        String email = data.getStringExtra("email");
        final String phone = data.getStringExtra("phone");

        previous = findViewById(R.id.previous);
        profilPic = findViewById(R.id.profil_image);
        profilFullName = findViewById(R.id.profilFullName);
        profilEmail = findViewById(R.id.emailTextView);
        profilPhone = findViewById(R.id.phoneTextView);
        saveBtn=findViewById(R.id.save_profil);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        userId=fAuth.getCurrentUser().getUid();

        storageReference = FirebaseStorage.getInstance().getReference();
        Log.d(TAG, "onCreate:"+fullName+" "+email+ " "+phone);
      //  final DocumentReference documentReference = fStore.collection("users").document(userId);
      //  final Map<String, Object> user = new HashMap<>();


        //user.put("fName ",fullName);
        //user.put("email ",email);
        //user.put("phone", phone);
       /* documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
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
*/

        previous.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), ProfilActivity.class);
                startActivity(i);
            }
        });

        profilPic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                     Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                      startActivityForResult(openGalleryIntent, 1000);

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(profilFullName.getText().toString().isEmpty() ||profilEmail.getText().toString().isEmpty() || profilPhone.getText().toString().isEmpty()){
                    Toast.makeText(EditProfil.this, "One or many fields are empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String email = profilEmail.getText().toString();
                final String fName = profilFullName.getText().toString();
                final String phone = profilPhone.getText().toString();

                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference docRef = fStore.collection("users").document(userId);
                        Map<String, Object> edited = new HashMap<>();
                        edited.put("fName ",fName);
                        edited.put("email ",email);
                        edited.put("phone", phone);
                        docRef.update(edited);

                        Toast.makeText(EditProfil.this, "Email is changed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfil.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        profilEmail.setText(email);
        profilFullName.setText(fullName);
        profilPhone.setText(phone);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageuri = data.getData();
                uploadImageToFirebase(imageuri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        //upload image to Firebase storage
        final StorageReference fileRef = storageReference.child("users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profile.jpg");

        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(EditProfil.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                  @Override
                                                                  public void onSuccess(Uri uri) {
                                                                      Picasso.get().load(uri).into(profilPic);
                                                                  }
                                                              }
                );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfil.this, "Failed", Toast.LENGTH_SHORT);

            }
        });
    }



}