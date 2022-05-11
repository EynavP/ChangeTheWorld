package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.changetheworld.model.BusinessCurrencyRateForShow;
import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.business_currency_rate;

import java.util.ArrayList;
import java.util.HashMap;

public class Business_rate_for_show extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    String business_user_name;
    ArrayList<BusinessCurrencyRateForShow> bcrs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_rate_for_show);
        recyclerView = findViewById(R.id.businessRatesRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar);
        business_user_name = getIntent().getStringExtra(getString(R.string.business_user_name));
        if(progressBar.getVisibility() != View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar = findViewById(R.id.progressBar);
        if(progressBar.getVisibility() != View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
        }
        FireStoreDB.getInstance().loadCurrencyRatesForShow(this, business_user_name, recyclerView, progressBar , bcrs);
    }

}