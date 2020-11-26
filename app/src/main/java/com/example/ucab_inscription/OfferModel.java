package com.example.ucab_inscription;

public class OfferModel {
    private String departure;
    private String destination;

    private OfferModel(){

    }
    private OfferModel(String departure, String destination){
            this.departure=departure;
            this.destination=destination;
    }
    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
