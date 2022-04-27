package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.business_currency_rate;

import java.util.ArrayList;
import java.util.HashMap;

public class Bussiness_rates extends AppCompatActivity{

    RecyclerView recyclerView;
    ProgressBar progressBar;
    String business_user_name;
    Button update;
    ArrayList<business_currency_rate> bcrs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussiness_rates);
        recyclerView = findViewById(R.id.businessRatesRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar);
        business_user_name = getIntent().getStringExtra(getString(R.string.userName));
        if(progressBar.getVisibility() != View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
        }
        update = findViewById(R.id.updateBtn);
        update.setOnClickListener(view -> {
            HashMap<String, String> comission_data = new HashMap<>();
            for (business_currency_rate bcr: bcrs) {
                comission_data.put(bcr.getCurrencyName(), bcr.getUpdate_sales_rate());
            }
            FireStoreDB.getInstance().saveChangeComissionRate(this, comission_data, business_user_name);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar = findViewById(R.id.progressBar);
        if(progressBar.getVisibility() != View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
        }
        FireStoreDB.getInstance().loadCurrencyRates(this, business_user_name, recyclerView, progressBar , bcrs);
    }


}