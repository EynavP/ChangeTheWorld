package com.example.changetheworld.model;

public class homePageCard {
    private String currencyName;
    private float value;
    private float dailyChange;

    public homePageCard(String currencyName, float value, float dailyChange) {
        this.currencyName = currencyName;
        this.value = value;
        this.dailyChange = dailyChange;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getDailyChange() {
        return dailyChange;
    }

    public void setDailyChange(float dailyChange) {
        this.dailyChange = dailyChange;
    }

    @Override
    public String toString(){
        return currencyName;
    }
}
