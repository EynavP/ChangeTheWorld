package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    TextView sundayHours, monThuHours, fridayHours, saturdayHours;


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

        sundayHours = findViewById(R.id.sundayHours);
        monThuHours = findViewById(R.id.monThuHours);
        fridayHours = findViewById(R.id.fridayHours);
        saturdayHours = findViewById(R.id.saturdayHours);

        userName = getIntent().getStringExtra(getString(R.string.userName));
        ((TextView)findViewById(R.id.business_username_profile_name)).setText(userName);
        FireStoreDB.getInstance().loadBusinessData(userName, business_name, mail_address, phone_number, owner_name, state, city, street, number, sundayHours, monThuHours, fridayHours, saturdayHours);


    }
}