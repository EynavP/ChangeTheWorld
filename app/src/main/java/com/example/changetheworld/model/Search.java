package com.example.changetheworld.model;

public class Search {

    String userName;
    String BusinessName;
    String rate;
    String OpenClose;
    String distance;

    String business_address;

    public Search(String userName, String businessName, String rate, String openClose, String business_address) {
        this.userName = userName;
        this.BusinessName = businessName;
        this.rate = rate;
        this.OpenClose = openClose;
        this.distance = "";
        this.business_address = business_address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBusinessName() { return BusinessName; }

    public void setBusinessName(String businessName) { BusinessName = businessName; }

    public String getRate() { return rate; }

    public void setRate(String rate) { this.rate = rate; }

    public String getOpenClose() { return OpenClose; }

    public void setOpenClose(String openClose) { OpenClose = openClose; }

    public String getDistance() { return distance; }

    public void setDistance(String distance) { this.distance = distance; }

    public String getBusiness_address() { return business_address; }

    public void setBusiness_address(String business_address) { this.business_address = business_address; }
}
