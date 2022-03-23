package com.example.changetheworld.model;

import java.util.ArrayList;
import java.util.List;

public class Wallet {

    String balance;
    String currency;
    String user_name;
    String symbol;
    String valueLocalCurrency;
    String symbolLocalCurrency;

    public Wallet(String balance, String currency, String user_name, String symbol, String valueLocalCurrency, String symbolLocalCurrency) {
        this.balance = balance;
        this.currency = currency;
        this.user_name = user_name;
        this.symbol = symbol;
        this.valueLocalCurrency = valueLocalCurrency;
        this.symbolLocalCurrency = symbolLocalCurrency;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getValueLocalCurrency() {
        return valueLocalCurrency;
    }

    public void setValueLocalCurrency(String valueLocalCurrency) {
        this.valueLocalCurrency = valueLocalCurrency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getSymbol() { return symbol; }

    public void setSymbol(String symbol) { this.symbol = symbol; }


    public String getSymbolLocalCurrency() { return symbolLocalCurrency; }

    public void setSymbolLocalCurrency(String symbolLocalCurrency) { this.symbolLocalCurrency = symbolLocalCurrency; }


}
