package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.changetheworld.model.FireStoreDB;

import java.lang.reflect.Field;

public class OrderDetails extends AppCompatActivity {

    TextView order_status_value, amount_value, currency_name_value, receive_value, to_currency_name_value, payment_method_value, client_name_value, phone_value, pickup_date_value;
    String orderID, user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        order_status_value = findViewById(R.id.order_status_value);
        amount_value = findViewById(R.id.amount_value);
        currency_name_value = findViewById(R.id.currency_name_value);
        receive_value = findViewById(R.id.receive_value);
        to_currency_name_value = findViewById(R.id.to_currency_name_value);
        payment_method_value = findViewById(R.id.payment_method_value);
        client_name_value = findViewById(R.id.client_name_value);
        phone_value = findViewById(R.id.phone_value);
        pickup_date_value = findViewById(R.id.pickup_date_value);

        orderID = getIntent().getStringExtra("orderID");
        user_name = getIntent().getStringExtra("user_name");

    }

    @Override
    protected void onResume() {
        super.onResume();

        FireStoreDB.getInstance().loadBusinessOrder(orderID, user_name, order_status_value, amount_value,
                currency_name_value, receive_value, to_currency_name_value, payment_method_value, client_name_value,
                phone_value, pickup_date_value);
    }
}