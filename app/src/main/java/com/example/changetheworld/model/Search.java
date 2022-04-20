package com.example.changetheworld.model;

public class Search {

    String BusinessName;
    String rate;
    String OpenClose;
    String distance;

    public Search(String businessName, String rate, String openClose, String distance) {
        BusinessName = businessName;
        this.rate = rate;
        OpenClose = openClose;
        this.distance = distance;
    }

    public String getBusinessName() { return BusinessName; }

    public void setBusinessName(String businessName) { BusinessName = businessName; }

    public String getRate() { return rate; }

    public void setRate(String rate) { this.rate = rate; }

    public String getOpenClose() { return OpenClose; }

    public void setOpenClose(String openClose) { OpenClose = openClose; }

    public String getDistance() { return distance; }

    public void setDistance(String distance) { this.distance = distance; }
}
