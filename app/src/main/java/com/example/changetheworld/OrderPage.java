package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changetheworld.model.FireStoreDB;

public class OrderPage extends AppCompatActivity {

    Spinner from, to;
    EditText amount;
    Button cash, wallet, submit;
    String payment_method, business_user_name, client_user_name;
    TextView receive, pick_from;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);
        business_user_name = getIntent().getStringExtra(getString(R.string.business_user_name));
        client_user_name = getIntent().getStringExtra(getString(R.string.client_user_name));

        from = findViewById(R.id.from_value);
        to = findViewById(R.id.to_value);
        amount = findViewById(R.id.amount_value);
        cash = findViewById(R.id.cash_btn);
        wallet = findViewById(R.id.wallet_btn);
        receive = findViewById(R.id.receive_value);
        pick_from = findViewById(R.id.pickup_from_value);
        submit = findViewById(R.id.submit_btn);

        cash.setOnClickListener(view -> {
            payment_method = "cash";
            cash.setBackgroundColor(Color.BLUE);
            wallet.setBackgroundColor(R.drawable.payment_method_btn);
        });
        wallet.setOnClickListener(view -> {
            payment_method = "wallet";
            wallet.setBackgroundColor(Color.BLUE);
            cash.setBackgroundColor(R.drawable.payment_method_btn);
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        adapter.addAll(FireStoreDB.getInstance().currenciesToSymbol.keySet());
        from.setAdapter(adapter);
        to.setAdapter(adapter);

        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty() && !from.getSelectedItem().toString().equals(to.getSelectedItem().toString())){
                    FireStoreDB.getInstance().calculateChangeRate(OrderPage.this, business_user_name, from.getSelectedItem().toString(),
                            to.getSelectedItem().toString(), Float.parseFloat(editable.toString()), receive);
                }
                else if(editable.toString().isEmpty()){
                    receive.setText("");
                    Toast.makeText(OrderPage.this, getString(R.string.Amount_shouldnt_be_zero) , Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(OrderPage.this, getString(R.string.currencies_must_be_diffrent), Toast.LENGTH_SHORT).show();
            }
        });








    }





}