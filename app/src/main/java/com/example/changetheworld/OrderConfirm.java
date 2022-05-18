package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.changetheworld.model.FireStoreDB;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class OrderConfirm extends AppCompatActivity {

    Button go_back_home, business_address;
    TextView amount_from, amount_to, paymethod, business_name, business_phone, pickup_date, cash_case_value, currency_from, currency_to;
    String user_type, orderID, business_user_name;
    ImageView QRcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        orderID = getIntent().getStringExtra(getString(R.string.orderID));
        business_user_name = orderID.split("\\*")[1];
        go_back_home = findViewById(R.id.homeBtn);
        cash_case_value = findViewById(R.id.cash_case_value);
        amount_to = findViewById(R.id.receive_value);
        amount_from = findViewById(R.id.amount_value);
        paymethod = findViewById(R.id.payment_method_value);
        business_name = findViewById(R.id.pickup_from_value);
        business_address = findViewById(R.id.pickup_address_value);
        business_phone = findViewById(R.id.phone_value);
        pickup_date = findViewById(R.id.pickup_date_value);
        currency_from = findViewById(R.id.currency_name_value);
        currency_to = findViewById(R.id.to_currency_name_value);
        QRcode = findViewById(R.id.QRcodeIV);
        user_type = getIntent().getStringExtra("user_type");

        business_address.setOnClickListener(view -> {
            FireStoreDB.getInstance().openOnMaps(this, business_address.getText().toString());
        });

        go_back_home.setOnClickListener(view -> {
            if(user_type.equals("PrivateClient")){
                Intent intent = new Intent(this, client_home_page.class);
                intent.putExtra(getString(R.string.userName),getIntent().getStringExtra("user_name"));
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(this, BusinessHomePage.class);
                intent.putExtra(getString(R.string.userName),getIntent().getStringExtra("user_name"));
                startActivity(intent);
            }
        });
        FireStoreDB.getInstance().LoadOrder(this ,orderID, business_user_name, amount_from, amount_to, paymethod, business_name, business_address, business_phone, pickup_date, cash_case_value, currency_from, currency_to);


        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(orderID, BarcodeFormat.QR_CODE,85,85);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            QRcode.setImageBitmap(bitmap);

        }catch (Exception e){
            e.printStackTrace();

        }


    }
}