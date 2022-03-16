package com.example.changetheworld.model;

import java.util.List;

class Wallet implements WalletInterface {
    float balance = 0;
    String currency;
    String user_name;

    public Wallet(float balance, String currency, String user_name) {
        this.balance = balance;
        this.currency = currency;
        this.user_name = user_name;
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
