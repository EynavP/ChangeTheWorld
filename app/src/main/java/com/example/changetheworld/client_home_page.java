package com.example.changetheworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
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
import java.util.concurrent.atomic.AtomicReference;

public class client_home_page<OnResume> extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
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
    ImageView search;
    AutoCompleteInterface aci = new AutoCompleteApi();
    AutoCompleteTextView autoCompleteTextView;
    SeekBar bar;
    TextView bar_text;

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
        recyclerView=findViewById(R.id.SubWalletRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar);

        FireStoreDB.getInstance().loadClientLocalCurrency(this, user_name, items, recyclerView, progressBar);

        profilPhoto = findViewById(R.id.profilePhoto);
        FireStoreDB.getInstance().LoadProfilePhoto(profilPhoto, user_name);
        userName.setText(user_name+",");


        autoCompleteTextView = findViewById(R.id.autoCompleteSearch);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString();
                if (query != null && !query.isEmpty()){
                    Thread t = new Thread(() -> {
                        ArrayList<String> result =  aci.getComplete(query);
                        runOnUiThread(() -> {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(client_home_page.this, android.R.layout.simple_dropdown_item_1line, result);
                            autoCompleteTextView.setAdapter(adapter);
                        });
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
        bar = findViewById(R.id.seekBar);
        bar_text = findViewById(R.id.seekBarText);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                bar_text.setText("KM "+ i) ;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        search = findViewById(R.id.search_button);
        search.setOnClickListener(view -> {
            String searchQuery = autoCompleteTextView.getText().toString();

            if ((searchQuery == null && searchQuery.isEmpty())|| bar_text.getText().length() <= 2){
                Toast.makeText(this, R.string.Invalid_location, Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(this, search_result_page.class);
                intent.putExtra("searchQuery", searchQuery);
                intent.putExtra("radius", bar_text.getText().toString());
                intent.putExtra(getString(R.string.client_user_name), user_name);
                startActivity(intent);
            }
        });
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

    public void openOrders(){

        Intent intent = new Intent(this,OrdersActivity.class);
        intent.putExtra(getString(R.string.userName), user_name);
        intent.putExtra("user_type", "PrivateClient");
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
            case R.id.nav_orders:
                openOrders();
        }
        return true;
    }
}

