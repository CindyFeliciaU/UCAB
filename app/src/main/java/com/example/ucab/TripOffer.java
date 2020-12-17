package com.example.ucab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TripOffer extends AppCompatActivity {

    Button confirmButton;
    Button cancelButton;
    EditText departure;
    EditText destination;
    TextView tripDate;
    TextView tripTime;
    EditText tripSeat;
    EditText commentTrip;
    EditText priceTrip;
    FirebaseDatabase fDatabase;
    DatabaseReference fRef;
    FirebaseUser user;
    String userId;
    FirebaseFirestore fStore;
    String TAG="TAG";
    Map<String, Object> trip_offer;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private Calendar c;
    private Context ctx = this;
    private FirebaseAuth fAuth;
    String customerName;
    //search

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_offer);
        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        userId=user.getUid();
        final Context context = this;
        confirmButton = findViewById(R.id.confirm_button);
        cancelButton = findViewById(R.id.cancel_button);
        departure= findViewById(R.id.departure);
        destination= findViewById(R.id.destination);
        tripDate = findViewById(R.id.date);
        tripTime=findViewById(R.id.time);
        tripSeat =findViewById(R.id.seat);
        commentTrip = findViewById(R.id.comments);
        priceTrip = findViewById(R.id.price);


        DocumentReference documentReference = fStore.collection("users").document(user.getUid());

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                customerName= (value.getString("fName "));
            }
        });

        mYear= Calendar.getInstance().get(Calendar.YEAR);
        mMonth=Calendar.getInstance().get(Calendar.MONTH)+1;
        mDay=Calendar.getInstance().get(Calendar.DAY_OF_MONTH) ;
        mHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) ;
        mMinute = Calendar.getInstance().get(Calendar.MINUTE);

        tripDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                show_Datepicker();
            }
        });

        tripTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_Timepicker();
            }
        });

        //Confirm the offer
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String depart = departure.getText().toString().trim();
                final String desti = destination.getText().toString().trim();
                final String date = tripDate.getText().toString().trim();
                final String time = tripTime.getText().toString().trim();
                final String seats = tripSeat.getText().toString().trim();
                final String price = priceTrip.getText().toString().trim();
                final String comment = commentTrip.getText().toString().trim();

               if (TextUtils.isEmpty(depart)) {
                    departure.setError("The departure is required");
                    return;
                }
                if (TextUtils.isEmpty(desti)) {
                    destination.setError("The destination is required");
                    return;
                }

                if (TextUtils.isEmpty(date)) {
                    tripDate.setError("The date is required");
                    return;
                }
                if (TextUtils.isEmpty(time)) {
                    tripTime.setError("The time is required");
                    return;
                }
                if (TextUtils.isEmpty(price)) {
                    priceTrip.setError("The price is required");
                    return;
                }

                if (TextUtils.isEmpty(seats)) {
                    tripSeat.setError("The number of seats available is required");
                    return;
                }

                trip_offer = new HashMap<>();
                trip_offer.put("userId", userId);
                trip_offer.put("departure", depart);
                trip_offer.put("destination", desti);
                trip_offer.put("date", date);
                trip_offer.put("time", time);
                trip_offer.put("seats", seats);
                trip_offer.put("price", price);
                trip_offer.put("comment", comment);



                if(customerName.isEmpty()) {
                    Toast.makeText(TripOffer.this, "Please enter your name before adding a trip", Toast.LENGTH_SHORT).show();

                    // get prompts.xml view
                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.text_inpu_password, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.editTextDialogUserInput);

                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // get user input and set it to result
                                            // edit text

                                            DocumentReference docRef = fStore.collection("users").document(fAuth.getUid());
                                            String name = userInput.getText().toString();

                                            docRef.update("fName ", name);
                                            fStore.collection("offer_trip")
                                                    .add(trip_offer)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
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
                                                            startActivity(new Intent(getApplicationContext(), OfferListAdapter.class));

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w(TAG, "Error adding the offer", e);
                                                        }
                                                    });

                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
                else{

                    fStore.collection("offer_trip")
                            .add(trip_offer)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    startActivity(new Intent(getApplicationContext(), OfferListAdapter.class));

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding the offer", e);
                                }
                            });

                    startActivity(new Intent(getApplicationContext(), OfferListAdapter.class));

                }


                }

             });


    }

    private void show_Datepicker() {
        c = Calendar.getInstance();
        int mYearParam = mYear;
        int mMonthParam = mMonth-1;
        int mDayParam = mDay;

        DatePickerDialog datePickerDialog = new DatePickerDialog(ctx,
                new DatePickerDialog.OnDateSetListener() {


                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mMonth = monthOfYear + 1;
                        mYear=year;
                        mDay=dayOfMonth;
                    }
                }, mYearParam, mMonthParam, mDayParam);

        datePickerDialog.show();
        tripDate.setText(mMonth + "/" + mDay +"/"+ mYear);
    
    }

    private void show_Timepicker() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(ctx,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int pHour,
                                          int pMinute) {

                        mHour = pHour;
                        mMinute = pMinute;
                    }
                }, mHour, mMinute, true);

        timePickerDialog.show();
        tripTime.setText( mHour + ":" +mMinute);

    }


    public String name(){


        DocumentReference documentReference = fStore.collection("users").document(user.getUid());


        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                customerName = (value.getString("fName "));
            }
        });
        return customerName;
    }

}




