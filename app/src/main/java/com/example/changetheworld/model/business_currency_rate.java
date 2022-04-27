package com.example.changetheworld.model;

public class business_currency_rate {

    private String symbolId;
    private String Exchangevalue;
    private String currencyName;
    private String Sales_value;
    private String update_sales_rate;

    public business_currency_rate(String symbolId, String exchangevalue, String currencyName, String sales_value, String update_sales_rate) {
        this.symbolId = symbolId;
        Exchangevalue = exchangevalue;
        this.currencyName = currencyName;
        Sales_value = sales_value;
        this.update_sales_rate = update_sales_rate;
    }

    public String getSymbolId() { return symbolId; }

    public void setSymbolId(String symbolId) { this.symbolId = symbolId; }

    public String getExchangevalue() { return Exchangevalue; }

    public void setExchangevalue(String exchangevalue) { Exchangevalue = exchangevalue; }

    public String getCurrencyName() { return currencyName; }

    public void setCurrencyName(String currencyName) { this.currencyName = currencyName; }

    public String getSales_value() { return Sales_value; }

    public void setSales_value(String sales_value) { Sales_value = sales_value; }

    public String getUpdate_sales_rate() { return update_sales_rate; }

    public void setUpdate_sales_rate(String update_sales_rate) { this.update_sales_rate = update_sales_rate; }
}
