package com.example.changetheworld.model;

public class Search {

    String userName;
    String BusinessName;
    String rate;
    String OpenClose;
    String distance;

    String business_state;
    String business_city;
    String business_street;
    String business_no;

    public Search(String userName, String businessName, String rate, String openClose, String business_state, String business_city, String business_street, String business_no) {
        this.userName = userName;
        this.BusinessName = businessName;
        this.rate = rate;
        this.OpenClose = openClose;
        this.distance = "";
        this.business_state = business_state;
        this.business_city = business_city;
        this.business_street = business_street;
        this.business_no = business_no;
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

    public String getBusiness_state() {
        return business_state;
    }

    public void setBusiness_state(String business_state) {
        this.business_state = business_state;
    }

    public String getBusiness_city() {
        return business_city;
    }

    public void setBusiness_city(String business_city) {
        this.business_city = business_city;
    }

    public String getBusiness_street() {
        return business_street;
    }

    public void setBusiness_street(String business_street) {
        this.business_street = business_street;
    }

    public String getBusiness_no() {
        return business_no;
    }

    public void setBusiness_no(String business_no) {
        this.business_no = business_no;
    }
}
