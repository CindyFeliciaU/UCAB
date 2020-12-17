package com.example.ucab;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ReceiptModel {
    public String destination;
    public String departure;
    public String tripId;
    public String customerName;
    public String driverName;
    public String date;
    FirebaseUser user;
     FirebaseFirestore fStore;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String price;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getCustomerName() {

        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String time;

    ReceiptModel(String tripId, String destination, String departure, String date, String time, String customerName, String driverName, String price) {
        this.destination = destination;
        this.departure = departure;
        this.tripId = tripId;
        this.date=date;
        this.driverName=driverName;
        this.customerName=customerName;
        this.time=time;
        this.price=price;
    }
    ReceiptModel( ) {

    }
}
