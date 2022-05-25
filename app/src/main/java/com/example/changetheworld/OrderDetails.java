package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changetheworld.model.FireStoreDB;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;

public class OrderDetails extends AppCompatActivity {

    TextView order_status_value, amount_value, currency_name_value, receive_value, to_currency_name_value, payment_method_value, client_name_value, phone_value, pickup_date_value;
    String orderID, user_name;
    Button approve_btn, cancel_btn,scan_btn;
    AtomicReference<String> user_type = new AtomicReference<>();


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

        approve_btn =findViewById(R.id.approve_btn);
        approve_btn.setOnClickListener(view -> {
            FireStoreDB.getInstance().changeOrderStatus(orderID, user_name, "approve", this, order_status_value, approve_btn, cancel_btn, payment_method_value, amount_value, currency_name_value);
        });

        cancel_btn = findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(view -> {
            FireStoreDB.getInstance().changeOrderStatus(orderID, user_name, "canceled", this, order_status_value, approve_btn, cancel_btn, payment_method_value, amount_value, currency_name_value);
        });

        scan_btn = findViewById(R.id.scan_btn);
        scan_btn.setOnClickListener(view -> {
            IntentIntegrator integrator = new IntentIntegrator((this));
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setPrompt("Scan");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(false);
            integrator.setOrientationLocked(true);
            integrator.initiateScan();
        });
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result!=null){
            if(result.getContents() == null){
                Toast.makeText(this,"Scan canceled",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this,"Scan Success",Toast.LENGTH_LONG).show();
                FireStoreDB.getInstance().CompleteOrder(this, result.getContents());
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        FireStoreDB.getInstance().loadBusinessOrder(orderID, user_name, order_status_value, amount_value,
                currency_name_value, receive_value, to_currency_name_value, payment_method_value, client_name_value,
                phone_value, pickup_date_value, approve_btn, cancel_btn, scan_btn);

    }


}