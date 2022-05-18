package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.Order;

import java.util.ArrayList;

public class BusinessOrdersActivity extends AppCompatActivity implements RecycleSubWalletClickInterface {

    RecyclerView recyclerView;
    String user_name;
    String user_type;
    ArrayList<Order> items;
    Button orders_as_client, orders_as_business;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_orders);


        user_name = getIntent().getStringExtra(getString(R.string.userName));
        user_type = getIntent().getStringExtra("user_type");
        recyclerView = findViewById(R.id.ordersRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orders_as_client = findViewById(R.id.my_orders_btn);
        orders_as_business = findViewById(R.id.clients_orders_btn);

        orders_as_client.setOnClickListener(view -> {
            items = new ArrayList<>();
            FireStoreDB.getInstance().loadOrdersAsClient(this, user_name, user_type, items, recyclerView);
        });
        orders_as_business.setOnClickListener(view -> {
            items = new ArrayList<>();
            FireStoreDB.getInstance().loadOrdersAsBusiness(this, user_name, user_type, items, recyclerView);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        items = new ArrayList<>();
        //FireStoreDB.getInstance().loadOrdersAsClient(this, user_name, user_type, items, recyclerView);
        FireStoreDB.getInstance().loadOrdersAsBusiness(this, user_name, user_type, items, recyclerView);
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