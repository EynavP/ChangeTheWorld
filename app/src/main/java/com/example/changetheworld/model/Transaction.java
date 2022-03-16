package com.example.changetheworld.model;

import com.google.type.DateTime;

public class Transaction {
    String sender;
    String receiver;
    float amount;
    DateTime create_time;

    public Transaction(String sender, String receiver, float amount, DateTime create_time) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.create_time = create_time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public DateTime getCreate_time() {
        return create_time;
    }

    public void setCreate_time(DateTime create_time) {
        this.create_time = create_time;
    }
}
