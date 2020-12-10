package com.example.ucab_inscription;

import android.widget.ImageView;

public class OfferModel {
    public String destination;
    public String departure;
    public String tripId;
    public String date;
    public String seats;
    public String price;
    public String time;
    public String driver;

    OfferModel(String tripId, String destination, String departure, String date, String time, String seats, String price, String driver) {
        this.destination = destination;
        this.departure = departure;
        this.tripId = tripId;
        this.date=date;
        this.time=time;
        this.seats=seats;
        this.price=price;
        this.driver=driver;
    }
    OfferModel( ) {

    }

    public String getDriver(){
        return driver;
    }
    public void setDriver(String driver){
        this.driver= driver;
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

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
    }

    public String getDeparture() {
        return departure;
    }

    public String getTripId() {

        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

}
