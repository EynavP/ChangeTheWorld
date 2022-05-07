package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.changetheworld.model.FireStoreDB;

public class BusinessProfileActivity extends AppCompatActivity {

    String userName;
    TextView business_name;
    TextView mail_address;
    TextView phone_number;
    TextView owner_name;
    TextView header;
    TextView local_currency;
    TextView address;

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
        address = findViewById(R.id.state_value);
        local_currency = findViewById(R.id.local_currency_value);

        sundayHours = findViewById(R.id.sundayHours);
        monThuHours = findViewById(R.id.monThuHours);
        fridayHours = findViewById(R.id.fridayHours);
        saturdayHours = findViewById(R.id.saturdayHours);

        userName = getIntent().getStringExtra(getString(R.string.userName));
        ((TextView)findViewById(R.id.business_username_profile_name)).setText(userName);
        editBtn = findViewById(R.id.editBtn);
        editBtn.setOnClickListener(view -> {OpenEditProfileBusiness();});
    }

    private void OpenEditProfileBusiness() {
        Intent intent = new Intent(this,EditBusinessProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("user_name", userName);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FireStoreDB.getInstance().loadBusinessData(userName, header, business_name, mail_address, phone_number, owner_name, address, local_currency, sundayHours, monThuHours, fridayHours, saturdayHours);

    }
}