package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;


import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.changetheworld.model.CurrencyDataApi;
import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.currency;

import java.util.ArrayList;

public class client_home_page extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterCurrency adapterCurrency;
    ArrayList<currency> items = new ArrayList<>();
    TextView userName;
    ImageView profilPhoto;
    CurrencyDataApi api = new CurrencyDataApi();
    Button moveToWallet;
    String user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home_page);
        user_name = getIntent().getStringExtra(getString(R.string.userName));
        userName = findViewById(R.id.username);
        String localCurrency = getIntent().getStringExtra(getString(R.string.localCurrency));
        userName.setText(user_name);

        Thread t = new Thread(() -> {

            Float value_usd = api.GetCurrencyValue(getString(R.string.USD),localCurrency);
            Float change_usd = api.GetCurrencyDailyChange(getString(R.string.USD), localCurrency);
            Float value_euro = api.GetCurrencyValue(getString(R.string.EUR),localCurrency);
            Float change_euro = api.GetCurrencyDailyChange(getString(R.string.EUR), localCurrency);
            Float value_cny = api.GetCurrencyValue(getString(R.string.CNY),localCurrency);
            Float change_cny = api.GetCurrencyDailyChange(getString(R.string.CNY), localCurrency);
            Float value_ils = api.GetCurrencyValue(getString(R.string.ILS),localCurrency);
            Float change_ils = api.GetCurrencyDailyChange(getString(R.string.ILS), localCurrency);
            Float value_gbp = api.GetCurrencyValue(getString(R.string.GBP),localCurrency);
            Float change_gbp = api.GetCurrencyDailyChange(getString(R.string.GBP), localCurrency);

            runOnUiThread(() -> {

                if (!localCurrency.equals(this.getString(R.string.USD)))
                    items.add(new currency(R.drawable.usd,""+value_usd,""+change_usd));
                if (!localCurrency.equals(getString(R.string.EUR)))
                    items.add(new currency(R.drawable.eur,""+value_euro,""+change_euro));
                if (!localCurrency.equals(getString(R.string.CNY)))
                    items.add(new currency(R.drawable.cny,""+value_cny,""+change_cny));
                if (!localCurrency.equals(getString(R.string.ILS)))
                    items.add(new currency(R.drawable.ils,""+value_ils,""+change_ils));
                if (!localCurrency.equals(getString(R.string.GBP)))
                    items.add(new currency(R.drawable.gbp,""+value_gbp,""+change_gbp));

                profilPhoto = findViewById(R.id.profilePhoto);
                FireStoreDB.getInstance().LoadProfilePhoto(profilPhoto, user_name);
                recyclerView=findViewById(R.id.recycle);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                adapterCurrency = new AdapterCurrency(this,items);
                recyclerView.setAdapter(adapterCurrency);
            });

        });
        t.start();

        moveToWallet = (Button) findViewById(R.id.moveToWallet);
        moveToWallet.setOnClickListener(view ->  { openWallet(); });
    }

    public void openWallet(){

        Intent intent = new Intent(this,WalletActivity.class);
        intent.putExtra(getString(R.string.userName), user_name);
        startActivity(intent);
    }
}

