package com.example.changetheworld.model;

import com.google.type.DateTime;

public class Transaction {
    String amount;
    String create_time;
    String action;

    public Transaction(String amount, String create_time, String action) {
        this.amount = amount;
        this.create_time = create_time;
        this.action = action;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
