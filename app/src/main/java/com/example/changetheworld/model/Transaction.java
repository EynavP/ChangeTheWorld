package com.example.changetheworld.model;


import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction implements Comparable<Transaction>{
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


    @Override
    public int compareTo(Transaction transaction) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date x = formatter.parse(this.create_time.replace("T"," "));
            Date y = formatter.parse(transaction.create_time.replace("T"," "));
            return y.compareTo(x);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
