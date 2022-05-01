package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.changetheworld.model.FireStoreDB;

public class BusinessPage extends AppCompatActivity {

    String userName;
    TextView business_name;
    TextView mail_address;
    TextView phone_number;
    TextView owner_name;
    TextView state;
    TextView city;
    TextView street;
    TextView number;
    TextView header;

    TextView sundayHours, monThuHours, fridayHours, saturdayHours;

    Button newOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_page);


        business_name = findViewById(R.id.business_name_value);
        mail_address = findViewById(R.id.maillAddress_value);
        phone_number = findViewById(R.id.phoneNumber_value);
        state = findViewById(R.id.state_value);
        city = findViewById(R.id.city_value);
        street = findViewById(R.id.street_value);
        number = findViewById(R.id.number_value);
        header =  findViewById(R.id.business_username_profile_name);

        sundayHours = findViewById(R.id.sundayHours);
        monThuHours = findViewById(R.id.monThuHours);
        fridayHours = findViewById(R.id.fridayHours);
        saturdayHours = findViewById(R.id.saturdayHours);

        userName = getIntent().getStringExtra("user_name");
        FireStoreDB.getInstance().loadBusinessData(userName,header, business_name, mail_address, phone_number, owner_name, state, city, street, number, sundayHours, monThuHours, fridayHours, saturdayHours);


        newOrder = findViewById(R.id.new_order_btn);
        newOrder.setOnClickListener(view -> {
            Intent intent = new Intent(this,OrderPage.class);
            startActivity(intent);
        });
    }
}