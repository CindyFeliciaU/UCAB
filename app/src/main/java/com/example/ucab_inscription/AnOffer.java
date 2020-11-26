package com.example.ucab_inscription;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;

public class AnOffer  {
    public String destination;
    public String departure;
    public String tripId;

    AnOffer(String tripId,String destination, String departure) {
        this.destination=destination;
        this.departure=departure;
        this.tripId=tripId;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

}