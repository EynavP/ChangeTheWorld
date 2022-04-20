package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.changetheworld.model.FireStoreDB;

public class ClientProfileActivity extends AppCompatActivity {

    String userName;
    TextView full_name;
    TextView mail_address;
    TextView phone_number;
    TextView local_currency;
    ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);
        full_name = findViewById(R.id.fullname_value);
        mail_address = findViewById(R.id.maillAddress_value);
        phone_number = findViewById(R.id.phoneNumber_value);
        local_currency = findViewById(R.id.localCurrency_value);
        userName = getIntent().getStringExtra(getString(R.string.userName));
        ((TextView)findViewById(R.id.username_profile_name)).setText(userName);
        FireStoreDB.getInstance().loadClientData(userName, full_name, mail_address, phone_number , local_currency);
        profile = findViewById(R.id.profilePhoto);
        FireStoreDB.getInstance().LoadProfilePhoto(profile, userName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FireStoreDB.getInstance().loadClientData(userName, full_name, mail_address, phone_number , local_currency);
        profile = findViewById(R.id.profilePhoto);
        FireStoreDB.getInstance().LoadProfilePhoto(profile, userName);
    }
}