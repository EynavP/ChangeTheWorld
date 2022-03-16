package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.changetheworld.model.Order;

import java.util.Date;
import java.util.ArrayList;

public class BusinessHomePage extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Order> items;
    AdapterOrder adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_home_page);

        items = new ArrayList<>();
        items.add(new Order(items.size()+1,"Dollar","Euro",100,new Date(),"Eynav","Koral","active",null,"Cash"));
        //TODO: Add orders to items array list

        recyclerView=findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterOrder(this,items);
        recyclerView.setAdapter(adapter);
    }
}