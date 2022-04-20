package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.changetheworld.model.FireStoreDB;

public class search_result_page extends AppCompatActivity {

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_page);

        recyclerView = findViewById(R.id.SearchRecycle);

        String state = getIntent().getStringExtra("state");
        String city = getIntent().getStringExtra("city");
        String street = getIntent().getStringExtra("street");
        String number = getIntent().getStringExtra("number");
        
        FireStoreDB.getInstance().searchChange(state, city, street, number, recyclerView, this);

    }
}