package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.changetheworld.model.FireStoreDB;

public class BusinessProfileActivity extends AppCompatActivity {

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

    Button editBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile);

        business_name = findViewById(R.id.business_name_value);
        mail_address = findViewById(R.id.maillAddress_value);
        phone_number = findViewById(R.id.phoneNumber_value);
        owner_name = findViewById(R.id.business_owner_name_value);
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

        editBtn = findViewById(R.id.editBtn);
        editBtn.setOnClickListener(view -> {OpenEditProfileBusiness();});
    }

    private void OpenEditProfileBusiness() {
        Intent intent = new Intent(this,EditBusinessProfileActivity.class);
        intent.putExtra("user_name", userName);
        startActivity(intent);
    }
}