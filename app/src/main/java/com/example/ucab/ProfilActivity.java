package com.example.ucab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfilActivity extends AppCompatActivity {

    private static final String USERS = "authentification/users";
    private TextView phoneTextView;
    private TextView emailTextView;
    private TextView nameTextView;
    private TextView scoreTextView;
    private Button changeProfilImage;
    private ImageView profilImage;
    StorageReference storageReference;
    FirebaseFirestore fStore;
    FirebaseUser user;
    private static final String TAG ="TAG" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        changeProfilImage = findViewById(R.id.change_profil_image);
        profilImage = findViewById(R.id.profil_image);
        scoreTextView = findViewById(R.id.scoreView);
        storageReference = FirebaseStorage.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        fStore = FirebaseFirestore.getInstance();


        StorageReference profileRef =storageReference.child("users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profile.jpg");
        DocumentReference documentReference = fStore.collection("users").document(user.getUid());

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                phoneTextView.setText(value.getString("phone"));
                emailTextView.setText(value.getString("email "));
                if(!value.getString("fName ").isEmpty())
                nameTextView.setText(value.getString("fName "));
                scoreTextView.setText(value.get("score").toString());
                String y= value.getString("phone");
                String i= value.getString("email ");

                String ii= value.getString("fName ");
            }
        });
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), "password1234");
        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "User re-authenticated.");
            }
        });

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
            @Override
            public void onSuccess(Uri uri){
                Picasso.get().load(uri).into(profilImage);
            }
        });

        if (user != null) {
            // Name, email address, and profile photo Url
            String mail = user.getEmail();
            emailTextView.setText(mail);
        }

        changeProfilImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //     Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //      startActivityForResult(openGalleryIntent, 1000);
                Intent i = new Intent(view.getContext(), EditProfil.class);
                i.putExtra("fullName", nameTextView.getText().toString());
                i.putExtra("email", emailTextView.getText().toString());
                i.putExtra("phone",phoneTextView.getText().toString());
                startActivity(i);
            }
        });


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
                Toast.makeText(ProfilActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profilImage);
                    }
                }
                );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfilActivity.this, "Failed", Toast.LENGTH_SHORT);

            }
        });
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
        }

        return super.onOptionsItemSelected(item);
    }

}