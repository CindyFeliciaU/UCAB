package com.example.ucab;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MyTripOffer extends AppCompatActivity {

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

    //search

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trip_offer);


        fDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        userId=user.getUid();
        Intent data = getIntent();

        final String list_hour = data.getStringExtra("list_hour");
        String list_price = data.getStringExtra("list_price");
        final String list_seats = data.getStringExtra("list_seats");
        final String list_date = data.getStringExtra("list_date");
        String list_departure = data.getStringExtra("list_departure");
        final String list_destination = data.getStringExtra("list_destination");
        final String ItemId=data.getStringExtra("ItemId");


        confirmButton = findViewById(R.id.confirm_button);
        cancelButton = findViewById(R.id.cancel_button);
        departure= findViewById(R.id.departure);
        destination= findViewById(R.id.destination);
        tripDate = findViewById(R.id.date);
        tripTime=findViewById(R.id.time);
        tripSeat =findViewById(R.id.seat);
        commentTrip = findViewById(R.id.comments);
        priceTrip = findViewById(R.id.price);

        tripTime.setText(list_hour);
        priceTrip.setText(list_price);
        tripSeat.setText(list_seats);
        tripTime.setText(list_hour);
        destination.setText(list_departure);
        departure.setText(list_departure);
        tripDate.setText(list_date);
        departure.setText(list_destination);

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

                DocumentReference docRef = fStore.collection("offer_trip").document(ItemId);

                Map<String, Object> trip_offer = new HashMap<>();
                trip_offer.put("userId", userId);
                trip_offer.put("departure", depart);
                trip_offer.put("destination", desti);
                trip_offer.put("date", date);
                trip_offer.put("time", time);
                trip_offer.put("seats", seats);
                trip_offer.put("price", price);
                trip_offer.put("comment", comment);
                docRef.update(trip_offer);
                Intent i = new Intent(getApplicationContext(), HistoryOffers.class);
                startActivity(i);
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



}




