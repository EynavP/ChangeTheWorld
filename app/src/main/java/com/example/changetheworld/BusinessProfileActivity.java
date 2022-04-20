package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class BusinessProfileActivity extends AppCompatActivity {

    Button editBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile);

        editBtn = findViewById(R.id.editBtn);
        editBtn.setOnClickListener(view -> {OpenEitProfileBusiness();});
    }

    private void OpenEitProfileBusiness() {
        Intent intent = new Intent(this,EditBusinessProfileActivity.class);
        startActivity(intent);
    }
}