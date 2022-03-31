package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.changetheworld.model.Order;

import java.util.Date;
import java.util.ArrayList;

public class BusinessHomePage extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Order> items;
    AdapterOrder adapter;
    TextView userName;
    String user_name;
    Button moveToWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_home_page);

        userName = findViewById(R.id.username);
        user_name = getIntent().getStringExtra(getString(R.string.userName));
        userName.setText(user_name);

        items = new ArrayList<>();
        items.add(new Order(items.size()+1,getString(R.string.USD),getString(R.string.EUR),100,new Date(),"Eynav","Koral","active",null,"Cash"));
        //TODO: Add orders to items array list

        recyclerView=findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterOrder(this,items);
        recyclerView.setAdapter(adapter);

        moveToWallet = (Button) findViewById(R.id.moveToWallet);
        moveToWallet.setOnClickListener(view ->  { openWallet(); });
    }

    public void openWallet(){

        Intent intent = new Intent(this,WalletActivity.class);
        intent.putExtra(getString(R.string.userName), user_name);
        intent.putExtra("user_type", "BusinessClient");
        startActivity(intent);
    }
}