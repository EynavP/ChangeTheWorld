package com.example.changetheworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;


import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import com.example.changetheworld.model.AutoCompleteApi;
import com.example.changetheworld.model.AutoCompleteInterface;
import com.example.changetheworld.model.CurrencyDataApi;
import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.currency;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;

public class client_home_page<OnResume> extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    AdapterCurrency adapterCurrency;
    ArrayList<currency> items = new ArrayList<>();
    TextView userName;
    ImageView profilPhoto;
    CurrencyDataApi api = new CurrencyDataApi();
    String user_name;
    ProgressBar progressBar;
    HashMap<String, String> currenciesToSymbol = new HashMap<>();
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Button search;
    EditText state;
    AutoCompleteInterface aci = new AutoCompleteApi();

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

        search = findViewById(R.id.search_button);
        search.setOnClickListener(view -> {
            String state = ((EditText) findViewById(R.id.state)).getText().toString();
            String city = ((EditText) findViewById(R.id.city)).getText().toString();
            String street = ((EditText) findViewById(R.id.street)).getText().toString();
            String number = ((EditText) findViewById(R.id.no)).getText().toString();
            if (state.isEmpty() || city.isEmpty()){
                Toast.makeText(this, R.string.invalid_state_or_city, Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(this, search_result_page.class);
                intent.putExtra("state", state);
                intent.putExtra("city", city);
                intent.putExtra("street", street);
                intent.putExtra("number", number);
                startActivity(intent);
            }
            });

        Thread t = new Thread(() -> {

            ArrayList<String> symbols = new ArrayList<>();
            if(!getString(R.string.USD).equals(localCurrency))
                symbols.add(getString(R.string.USD) +'/'+localCurrency);
            if(!getString(R.string.EUR).equals(localCurrency))
                symbols.add(getString(R.string.EUR) +'/'+localCurrency);
            if(!getString(R.string.CNY).equals(localCurrency))
                symbols.add(getString(R.string.CNY) +'/'+localCurrency);
            if(!getString(R.string.ILS).equals(localCurrency))
                symbols.add(getString(R.string.ILS) +'/'+localCurrency);
            if(!getString(R.string.GBP).equals(localCurrency))
                symbols.add(getString(R.string.GBP) +'/'+localCurrency);

            HashMap<String, ArrayList<Float>> currency_data = api.getCloseAndChangePrice(symbols);



            runOnUiThread(() -> {

                if (!localCurrency.equals(this.getString(R.string.USD)) && currency_data.get(getString(R.string.USD)) != null && currency_data.get(getString(R.string.USD)).size() > 0)
                    items.add(new currency(R.drawable.usd,""+currency_data.get(getString(R.string.USD)).get(0) + currenciesToSymbol.get(localCurrency),""+(currency_data.get(getString(R.string.USD)).get(1)) + "%", this.getString(R.string.USD)));
                if (!localCurrency.equals(getString(R.string.EUR)) && currency_data.get(getString(R.string.EUR)) != null && currency_data.get(getString(R.string.EUR)).size() > 0)
                    items.add(new currency(R.drawable.eur,""+currency_data.get(getString(R.string.EUR)).get(0) + currenciesToSymbol.get(localCurrency),""+(currency_data.get(getString(R.string.EUR)).get(1)) + '%', this.getString(R.string.EUR)));
                if (!localCurrency.equals(getString(R.string.CNY)) && currency_data.get(getString(R.string.CNY)) != null && currency_data.get(getString(R.string.CNY)).size() > 0)
                    items.add(new currency(R.drawable.cny,""+currency_data.get(getString(R.string.CNY)).get(0) + currenciesToSymbol.get(localCurrency),""+(currency_data.get(getString(R.string.CNY)).get(1)) + '%', this.getString(R.string.CNY)));
                if (!localCurrency.equals(getString(R.string.ILS)) && currency_data.get(getString(R.string.ILS)) != null && currency_data.get(getString(R.string.ILS)).size() > 0)
                    items.add(new currency(R.drawable.ils,""+currency_data.get(getString(R.string.ILS)).get(0) + currenciesToSymbol.get(localCurrency),""+(currency_data.get(getString(R.string.ILS)).get(1)) + '%', this.getString(R.string.ILS)));
                if (!localCurrency.equals(getString(R.string.GBP)) && currency_data.get(getString(R.string.GBP)) != null && currency_data.get(getString(R.string.GBP)).size() > 0)
                    items.add(new currency(R.drawable.gbp,""+currency_data.get(getString(R.string.GBP)).get(0) + currenciesToSymbol.get(localCurrency),""+(currency_data.get(getString(R.string.GBP)).get(1)) + '%', this.getString(R.string.GBP)));

                profilPhoto = findViewById(R.id.profilePhoto);
                FireStoreDB.getInstance().LoadProfilePhoto(profilPhoto, user_name);
                recyclerView=findViewById(R.id.SubWalletRecycle);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                adapterCurrency = new AdapterCurrency(this,items);
                recyclerView.setAdapter(adapterCurrency);
                progressBar.setVisibility(View.INVISIBLE);
            });

        });
        t.start();


        state = findViewById(R.id.state);
        state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString();
                if (query != null && !query.isEmpty()){
                    Thread t = new Thread(() -> {
                        ArrayList<String> result = aci.getComplete(query);
                        result.forEach(r -> Log.d("Yuval", "afterTextChanged: " + r));
                    });
                    t.start();
                }
            }
        });

        drawerLayout = findViewById(R.id.drawer_menu);
        navigationView = findViewById(R.id.nav_view);
        toolbar =(Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

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
        intent.putExtra("user_type", "PrivateClient");
        startActivity(intent);
    }

    public void openProfile(){

        Intent intent = new Intent(this,ClientProfileActivity.class);
        intent.putExtra(getString(R.string.userName), user_name);
        startActivity(intent);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.nav_home:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_wallet:
                openWallet();
                break;
            case R.id.nav_profile:
                openProfile();
                break;
        }
        return true;
    }
}

