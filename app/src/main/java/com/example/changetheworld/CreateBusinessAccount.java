package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CreateBusinessAccount extends AppCompatActivity {
    Spinner states;
    String[] state = {"Choose State","Israel","United States","Brazil","Italy"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_business_account);

        states = (Spinner) findViewById(R.id.enterBusinessState);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        adapter.addAll(state);
        states.setAdapter(adapter);

 }

}