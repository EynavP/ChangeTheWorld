package com.example.changetheworld;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.changetheworld.model.homePageCard;

public class LinkedCardsListAdapter extends ArrayAdapter<homePageCard> {
    public LinkedCardsListAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull homePageCard[] objects) {
        super(context, resource, textViewResourceId, objects);
    }
}
