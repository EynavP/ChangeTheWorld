package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.changetheworld.model.CurrencyDataApi;
import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.currency;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class client_home_page extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterCurrency adapterCurrency;
    ArrayList<currency> items = new ArrayList<>();
    TextView userName;
    ImageView profilPhoto;
    CurrencyDataApi api = new CurrencyDataApi();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home_page);

        userName = findViewById(R.id.username);
        String user_name = getIntent().getStringExtra("userName");
        userName.setText(user_name);

        Thread t = new Thread(() -> {
            Float value_usd = api.GetCurrencyValue("USD","ILS");
            Float change_usd = api.GetCurrencyDailyChange("USD", "ILS");
            Float value_euro = api.GetCurrencyValue("EUR","ILS");
            Float change_euro = api.GetCurrencyDailyChange("EUR", "ILS");

            items.add(new currency(R.drawable.dollar,""+value_usd,""+change_usd));
            items.add(new currency(R.drawable.euro,""+value_euro,""+change_euro));
        });
        t.start();
        try {
            t.join();
            String usd = items.get(0).getDailyChange();
            String eur = items.get(1).getDailyChange();
            if (usd.equals("0.0") || eur.equals("0.0")){
                Toast.makeText(this,"Failed to connect currency api",Toast.LENGTH_LONG).show();
            }

            recyclerView=findViewById(R.id.recycle_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapterCurrency = new AdapterCurrency(this,items);
            recyclerView.setAdapter(adapterCurrency);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        profilPhoto = findViewById(R.id.profilePhoto);
        FireStoreDB.getInstance().LoadProfilePhoto(profilPhoto, user_name);

    }
}