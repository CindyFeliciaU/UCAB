package com.example.ucab_inscription;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;

import java.util.Arrays;
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
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private Calendar c;
    private Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_offer);
        fDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        userId=user.getUid();

        confirmButton = findViewById(R.id.confirm_button);
        cancelButton = findViewById(R.id.cancel_button);
        departure= findViewById(R.id.departure);
        destination= findViewById(R.id.destination);
        tripDate = findViewById(R.id.date);
        tripTime=findViewById(R.id.time);
        tripSeat =findViewById(R.id.seat);
        commentTrip = findViewById(R.id.comments);
        priceTrip = findViewById(R.id.price);

        mYear= Calendar.getInstance().get(Calendar.YEAR);
        mMonth=Calendar.getInstance().get(Calendar.MONTH)+1;
        mDay=Calendar.getInstance().get(Calendar.DAY_OF_MONTH) ;
        mHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) ;
        mMinute = Calendar.getInstance().get(Calendar.MINUTE);

        tripDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Calendar calendar = Calendar.getInstance();
            //    mYear = calendar.get(Calendar.YEAR);
            //    mMonth = calendar.get(Calendar.MONTH);
             //   mDay = calendar.get(Calendar.DAY_OF_MONTH);
               // DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, MainActivity.this,year, month,day);

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
                    departure.setError("Departure is required");
                    return;
                }
                if (TextUtils.isEmpty(desti)) {
                    destination.setError("Destination is required");
                    return;
                }

                Map<String, Object> trip_offer = new HashMap<>();
                trip_offer.put("userId", userId);
                trip_offer.put("departure", depart);
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
                                startActivity(new Intent(getApplicationContext(), HomeOffers.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding the offer", e);
                            }
                        });
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

  /*  /**
     * Add a document to a collection using a map.
     *
     * @return document data
     */
    /*
    Map<String, Object> addSimpleDocumentAsMap() throws Exception {
        // [START fs_add_doc_as_map]
        // [START firestore_data_set_from_map]
        // Create a Map to store the data we want to set
        Map<String, Object> docData = new HashMap<>();
        docData.put("name", "Los Angeles");
        docData.put("state", "CA");
        docData.put("country", "USA");
        docData.put("regions", Arrays.asList("west_coast", "socal"));
        // Add a new document (asynchronously) in collection "cities" with id "LA"
        ApiFuture<WriteResult> future = db.collection("cities").document("LA").set(docData);
        // ...
        // future.get() blocks on response
        System.out.println("Update time : " + future.get().getUpdateTime());
        // [END firestore_data_set_from_map]
        // [END fs_add_doc_as_map]
        return docData;
    }*/

}




