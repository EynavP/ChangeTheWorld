package com.example.changetheworld.model;

public class currency {
    private String currencyname;
    private String value;
    private String dailychange;

    public currency(String currencyname, String value, String dailychange) {
        this.currencyname = currencyname;
        this.value = value;
        this.dailychange = dailychange;
    }

    public String getCurrencyname() {
        return currencyname;
    }

    public void setCurrencyname(String currencyname) {
        this.currencyname = currencyname;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDailychange() {
        return dailychange;
    }

    public void setDailychange(String dailychange) {
        this.dailychange = dailychange;
    }
}
