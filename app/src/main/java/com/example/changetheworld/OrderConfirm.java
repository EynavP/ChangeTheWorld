package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.RateFlag;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.lang.reflect.Method;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class OrderConfirm extends AppCompatActivity {

    Button  business_address;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    RatingBar rating_bar;
    Button go_back_home,SubmitRate,cancleRate;
    TextView amount_from, amount_to, paymethod, business_name, business_phone, pickup_date, cash_case_value, currency_from, currency_to, status_value;
    String user_type, orderID, business_user_name, order_status;
    ImageView QRcode;
    float myRating;
    int rating;
    RateFlag rateFlag = new RateFlag();
    Observer observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        orderID = getIntent().getStringExtra(getString(R.string.orderID));
        status_value = findViewById(R.id.status_value);
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
        order_status = getIntent().getStringExtra("status");

        observer = new Observer() {
            @Override
            public void update(Observable observable, Object o) {
                createNewContantDialog();
            }
        };
        rateFlag.addObserver(observer);



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
        FireStoreDB.getInstance().LoadOrder(this ,orderID, business_user_name, amount_from, amount_to, paymethod, business_name, business_address, business_phone, pickup_date, cash_case_value, currency_from, currency_to, status_value);

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

    @Override
    protected void onResume() {
        super.onResume();
        FireStoreDB.getInstance().checkOrderRated(orderID, this, rateFlag);
    }


    public void  createNewContantDialog(){
        dialogBuilder= new AlertDialog.Builder(this);
        final View contactpopupView =getLayoutInflater().inflate(R.layout.ratepopup,null);

        rating_bar=(RatingBar) contactpopupView.findViewById(R.id.ratingBar);
        SubmitRate=(Button) contactpopupView.findViewById(R.id.BtnSubmitRate);
        cancleRate=(Button) contactpopupView.findViewById(R.id.cancelRate);

        dialogBuilder.setView(contactpopupView);
        dialog=dialogBuilder.create();
        dialog.show();


        SubmitRate.setOnClickListener(view -> {
                FireStoreDB.getInstance().updateBusinessRate(rating, business_user_name);
                dialog.dismiss();
        });


        rating_bar.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            rating=(int)v;
            String message=null;

            myRating= ratingBar.getRating();
            switch (rating){
                case 1:
                    message=getString(R.string.messageRate1);
                    break;
                case 2:
                    message=getString(R.string.messageRate2);
                    break;
                case 3:
                    message=getString(R.string.messageRate3);
                    break;
                case 4:
                    message=getString(R.string.messageRate4);
                    break;
                case 5:
                    message=getString(R.string.messageRate5);
                    break;
            }
            Toast.makeText(this,"Your rating is:"+ message,Toast.LENGTH_SHORT).show();
            FireStoreDB.getInstance().updateOrderRateStatus(orderID);
        });

        cancleRate.setOnClickListener(view -> {
            dialog.dismiss();
        });
    }
}