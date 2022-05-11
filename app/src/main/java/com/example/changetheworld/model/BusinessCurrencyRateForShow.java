package com.example.changetheworld.model;

public class BusinessCurrencyRateForShow {
    private String symbolId;
    private String currencyName;
    private String sales_price;
    private String buy_price;

    public BusinessCurrencyRateForShow(String symbolId, String currencyName, String sales_price, String buy_price) {
        this.symbolId = symbolId;
        this.currencyName = currencyName;
        this.sales_price = sales_price;
        this.buy_price = buy_price;
    }

    public String getSymbolId() {
        return symbolId;
    }

    public void setSymbolId(String symbolId) {
        this.symbolId = symbolId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getSales_price() {
        return sales_price;
    }

    public void setSales_price(String sales_price) {
        this.sales_price = sales_price;
    }

    public String getBuy_price() {
        return buy_price;
    }

    public void setBuy_price(String buy_price) {
        this.buy_price = buy_price;
    }
}
