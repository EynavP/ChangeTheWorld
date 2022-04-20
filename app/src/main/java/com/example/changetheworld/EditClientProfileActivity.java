package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.changetheworld.model.FireStoreDB;

public class EditClientProfileActivity extends AppCompatActivity {

    String userName;
    EditText full_name;
    EditText mail_address;
    EditText phone_number;
    EditText password;
    Spinner local_currency;
    ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client_profile);

        local_currency = findViewById(R.id.localCurrency_value);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        adapter.addAll(FireStoreDB.getInstance().currenciesToSymbol.keySet());
        local_currency.setAdapter(adapter);

        full_name = findViewById(R.id.fullname_value);
        mail_address = findViewById(R.id.maillAddress_value);
        phone_number = findViewById(R.id.phoneNumber_value);
        password = findViewById(R.id.password_value);
        userName = getIntent().getStringExtra("user_name");
        ((TextView)findViewById(R.id.username_profile_name)).setText(userName);
        FireStoreDB.getInstance().loadClientDataForEdit(userName, full_name, mail_address, phone_number , local_currency, password);
        profile = findViewById(R.id.profilePhoto);
        FireStoreDB.getInstance().LoadProfilePhoto(profile, userName);
    }

    protected void onResume() {
        super.onResume();
        FireStoreDB.getInstance().loadClientDataForEdit(userName, full_name, mail_address, phone_number , local_currency, password);
        profile = findViewById(R.id.profilePhoto);
        FireStoreDB.getInstance().LoadProfilePhoto(profile, userName);
    }
}