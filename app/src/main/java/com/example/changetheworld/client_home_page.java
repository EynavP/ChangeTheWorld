package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.changetheworld.model.CurrencyDataApi;
import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.currency;

import java.util.ArrayList;
import java.util.HashMap;

public class client_home_page<OnResume> extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterCurrency adapterCurrency;
    ArrayList<currency> items = new ArrayList<>();
    TextView userName;
    ImageView profilPhoto;
    CurrencyDataApi api = new CurrencyDataApi();
    Button moveToWallet;
    String user_name;
    ProgressBar progressBar;
    HashMap<String, String> currenciesToSymbol = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currenciesToSymbol.put("USD", " $");
        currenciesToSymbol.put("EUR", " €");
        currenciesToSymbol.put("GBP", " £");
        currenciesToSymbol.put("CNY", " ¥");
        currenciesToSymbol.put("ILS", " ₪");
        setContentView(R.layout.activity_client_home_page);
        user_name = getIntent().getStringExtra(getString(R.string.userName));
        userName = findViewById(R.id.username);
        String localCurrency = getIntent().getStringExtra(getString(R.string.localCurrency));
        userName.setText(user_name);
        progressBar = findViewById(R.id.progressBar);

        Thread t = new Thread(() -> {

            ArrayList<String> symbols = new ArrayList<>();
            symbols.add(getString(R.string.USD) +'/'+localCurrency);
            symbols.add(getString(R.string.EUR) +'/'+localCurrency);
            symbols.add(getString(R.string.CNY) +'/'+localCurrency);
            symbols.add(getString(R.string.ILS) +'/'+localCurrency);
            symbols.add(getString(R.string.GBP) +'/'+localCurrency);

            HashMap<String, ArrayList<Float>> currency_data = api.getCloseAndChangePrice(symbols);



            runOnUiThread(() -> {

                if (!localCurrency.equals(this.getString(R.string.USD)) && currency_data.get(getString(R.string.USD)) != null && currency_data.get(getString(R.string.USD)).size() > 0)
                    items.add(new currency(R.drawable.usd,""+currency_data.get(getString(R.string.USD)).get(0) + currenciesToSymbol.get(localCurrency),""+(currency_data.get(getString(R.string.USD)).get(1)) + "%"));
                if (!localCurrency.equals(getString(R.string.EUR)) && currency_data.get(getString(R.string.EUR)) != null && currency_data.get(getString(R.string.EUR)).size() > 0)
                    items.add(new currency(R.drawable.eur,""+currency_data.get(getString(R.string.EUR)).get(0) + currenciesToSymbol.get(localCurrency),""+(currency_data.get(getString(R.string.EUR)).get(1)) + '%'));
                if (!localCurrency.equals(getString(R.string.CNY)) && currency_data.get(getString(R.string.CNY)) != null && currency_data.get(getString(R.string.CNY)).size() > 0)
                    items.add(new currency(R.drawable.cny,""+currency_data.get(getString(R.string.CNY)).get(0) + currenciesToSymbol.get(localCurrency),""+(currency_data.get(getString(R.string.CNY)).get(1)) + '%'));
                if (!localCurrency.equals(getString(R.string.ILS)) && currency_data.get(getString(R.string.ILS)) != null && currency_data.get(getString(R.string.ILS)).size() > 0)
                    items.add(new currency(R.drawable.ils,""+currency_data.get(getString(R.string.ILS)).get(0) + currenciesToSymbol.get(localCurrency),""+(currency_data.get(getString(R.string.ILS)).get(1)) + '%'));
                if (!localCurrency.equals(getString(R.string.GBP)) && currency_data.get(getString(R.string.GBP)) != null && currency_data.get(getString(R.string.GBP)).size() > 0)
                    items.add(new currency(R.drawable.gbp,""+currency_data.get(getString(R.string.GBP)).get(0) + currenciesToSymbol.get(localCurrency),""+(currency_data.get(getString(R.string.GBP)).get(1)) + '%'));

                profilPhoto = findViewById(R.id.profilePhoto);
                FireStoreDB.getInstance().LoadProfilePhoto(profilPhoto, user_name);
                recyclerView=findViewById(R.id.recycle);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                adapterCurrency = new AdapterCurrency(this,items);
                recyclerView.setAdapter(adapterCurrency);
                progressBar.setVisibility(View.INVISIBLE);
            });

        });
        t.start();


        moveToWallet = (Button) findViewById(R.id.moveToWallet);
        moveToWallet.setOnClickListener(view ->  { openWallet(); });
    }
    @Override
    public void onResume(){
        super.onResume();
        ProgressBar progressBar;
        progressBar = findViewById(R.id.progressBar);

        if(progressBar.getVisibility() != View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void openWallet(){

        Intent intent = new Intent(this,WalletActivity.class);
        intent.putExtra(getString(R.string.userName), user_name);
        startActivity(intent);
    }


}

