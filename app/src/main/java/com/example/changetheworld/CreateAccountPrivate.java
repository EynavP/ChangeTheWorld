package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateAccountPrivate extends AppCompatActivity {
    Spinner currencies;
    String[] currency = {"Choose Currency","DOLLAR","EURO"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_private);
        currencies = (Spinner) findViewById(R.id.enterLocalCurrency);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        adapter.addAll(currency);
        currencies.setAdapter(adapter);

    }
}