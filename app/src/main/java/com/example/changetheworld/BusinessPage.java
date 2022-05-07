package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.changetheworld.model.FireStoreDB;

public class BusinessPage extends AppCompatActivity {

    String userName, client_user_name;
    TextView business_name;
    TextView mail_address;
    TextView phone_number;
    TextView owner_name;
    TextView header;
    TextView address;
    TextView local_currency;


    TextView sundayHours, monThuHours, fridayHours, saturdayHours;

    Button newOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_page);


        business_name = findViewById(R.id.business_name_value);
        mail_address = findViewById(R.id.maillAddress_value);
        phone_number = findViewById(R.id.phoneNumber_value);
        header =  findViewById(R.id.business_username_profile_name);
        local_currency = findViewById(R.id.local_currency_value);
        address = findViewById(R.id.address_value);
        sundayHours = findViewById(R.id.sundayHours);
        monThuHours = findViewById(R.id.monThuHours);
        fridayHours = findViewById(R.id.fridayHours);
        saturdayHours = findViewById(R.id.saturdayHours);

        userName = getIntent().getStringExtra(getString(R.string.business_user_name));
        client_user_name = getIntent().getStringExtra(getString(R.string.client_user_name));
        FireStoreDB.getInstance().loadBusinessData(userName,header, business_name, mail_address, phone_number, owner_name, address, local_currency, sundayHours, monThuHours, fridayHours, saturdayHours);


        newOrder = findViewById(R.id.new_order_btn);
        newOrder.setOnClickListener(view -> {
            Intent intent = new Intent(this,OrderPage.class);
            intent.putExtra(getString(R.string.business_user_name), userName);
            intent.putExtra(getString(R.string.client_user_name), client_user_name);
            startActivity(intent);
        });
    }
}