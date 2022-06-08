package com.example.changetheworld.model;

import java.util.Observable;

public class RateFlag extends Observable {

    int rate =0;

    public void setRate(int rate) {
        this.rate = rate;
        setChanged();
        notifyObservers();
    }
}
