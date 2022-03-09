package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;


import com.example.changetheworld.model.currency;

import java.util.ArrayList;

public class client_home_page extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<currency> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home_page);

        items = new ArrayList<>();
        items.add(new currency("Dollar","3.6","0.30"));
        items.add(new currency("Euro","3.5","-0.35"));
        recyclerView=findViewById(R.id.recycle_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this,items);
        recyclerView.setAdapter(adapter);
    }
}