package com.example.changetheworld.model;

import java.util.ArrayList;
import java.util.List;

public class Wallet implements WalletInterface {

    float balance = 0;
    String currency;
    String user_name;
    String symbol;
    float valueLocalCurrency;
    String symbolLocalCurrency;

    public Wallet(float balance, String currency, String user_name) {
        this.balance = balance;
        this.currency = currency;
        this.user_name = user_name;
    }

    public Wallet(float balance, String currency, String user_name, String symbol, float valueLocalCurrency, String symbolLocalCurrency) {
        this.balance = balance;
        this.currency = currency;
        this.user_name = user_name;
        this.symbol = symbol;
        this.valueLocalCurrency = valueLocalCurrency;
        this.symbolLocalCurrency = symbolLocalCurrency;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
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

    public float getValueLocalCurrency() { return valueLocalCurrency; }

    public void setValueLocalCurrency(float valueLocalCurrency) { this.valueLocalCurrency = valueLocalCurrency; }

    public String getSymbolLocalCurrency() { return symbolLocalCurrency; }

    public void setSymbolLocalCurrency(String symbolLocalCurrency) { this.symbolLocalCurrency = symbolLocalCurrency; }

    @Override
    public void Deposit() {

    }

    @Override
    public void Withdraw() {

    }

    @Override
    public void GetHistory() {

    }





}
