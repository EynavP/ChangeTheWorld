package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.Order;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity implements RecycleSubWalletClickInterface {

    RecyclerView recyclerView;
    String user_name;
    String user_type;
    ArrayList<Order> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        user_name = getIntent().getStringExtra(getString(R.string.userName));
        user_type = getIntent().getStringExtra("user_type");
        recyclerView = findViewById(R.id.ordersRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();

        items = new ArrayList<>();
        FireStoreDB.getInstance().loadOrders(this, user_name, user_type, items, recyclerView);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, OrderConfirm.class);
        intent.putExtra("user_type",user_type);
        intent.putExtra("user_name",user_name);
        intent.putExtra("orderID", items.get(position).getId());
        startActivity(intent);
    }
}