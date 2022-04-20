package com.example.changetheworld.model;

import android.widget.ImageView;

public class currency {
    private int symbolId;
    private String value;
    private String dailyChange;
    private String currencyName;

    public currency(int symbolId, String value, String dailyChange, String currencyName) {
        this.symbolId = symbolId;
        this.value = value;
        this.dailyChange = dailyChange;
        this.currencyName = currencyName;
    }

    public int getSymbolId() {
        return symbolId;
    }

    public void setSymbolId(int symbolId) {
        this.symbolId = symbolId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDailyChange() {
        return dailyChange;
    }

    public void setDailyChange(String dailyChange) {
        this.dailyChange = dailyChange;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }
}

