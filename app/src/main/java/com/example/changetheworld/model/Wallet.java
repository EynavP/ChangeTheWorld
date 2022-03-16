package com.example.changetheworld.model;

import java.util.List;

class Wallet implements WalletInterface {
    float balance = 0;
    List<Transaction> history;
    String currency;
    String user_name;

    public Wallet(float balance, List<Transaction> history, String currency, String user_name) {
        this.balance = balance;
        this.history = history;
        this.currency = currency;
        this.user_name = user_name;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public List<Transaction> getHistory() {
        return history;
    }

    public void setHistory(List<Transaction> history) {
        this.history = history;
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
