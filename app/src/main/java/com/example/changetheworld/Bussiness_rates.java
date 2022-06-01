package com.example.changetheworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.business_currency_rate;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;

public class Bussiness_rates extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView recyclerView;
    ProgressBar progressBar;
    String business_user_name;
    Button update;
    NavigationView navigationView;
    Toolbar toolbar;
    ArrayList<business_currency_rate> bcrs = new ArrayList<>();
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussiness_rates);
        recyclerView = findViewById(R.id.businessRatesRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar);
        business_user_name = getIntent().getStringExtra(getString(R.string.userName));

        drawerLayout = findViewById(R.id.bussiness_rates);
        navigationView = findViewById(R.id.nav_view);
        toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        if(progressBar.getVisibility() != View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
        }
        update = findViewById(R.id.updateBtn);
        update.setOnClickListener(view -> {
            HashMap<String, String> comission_data = new HashMap<>();
            for (business_currency_rate bcr: bcrs) {
                comission_data.put(bcr.getCurrencyName(), bcr.getUpdate_sales_rate());
            }
            FireStoreDB.getInstance().saveChangeComissionRate(this, comission_data, business_user_name, false);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar = findViewById(R.id.progressBar);
        if(progressBar.getVisibility() != View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
        }
        FireStoreDB.getInstance().loadCurrencyRates(this, business_user_name, recyclerView, progressBar , bcrs);
    }


    public void openWallet(){

        Intent intent = new Intent(this,WalletActivity.class);
        intent.putExtra(getString(R.string.userName), business_user_name);
        intent.putExtra("user_type", "BusinessClient");
        startActivity(intent);
    }

    public void openBusinessProfile(){

        Intent intent = new Intent(this,BusinessProfileActivity.class);
        intent.putExtra(getString(R.string.userName), business_user_name);
        startActivity(intent);
    }

    private void openBusinessHomepage() {
        Intent intent = new Intent(this,BusinessHomePage.class);
        intent.putExtra(getString(R.string.userName), business_user_name);
        startActivity(intent);
    }

    public void openBusinessOrders(){
        Intent intent = new Intent(this,BusinessOrdersActivity.class);
        intent.putExtra(getString(R.string.userName), business_user_name);
        intent.putExtra("user_type", "BusinessClient");
        startActivity(intent);
    }

    public void logOut(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.nav_home:
                openBusinessHomepage();
                break;
            case R.id.nav_wallet:
                openWallet();
                break;
            case R.id.nav_profile:
                openBusinessProfile();
                break;
            case R.id.nav_update_rates:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_orders:
                openBusinessOrders();
                break;
            case R.id.nav_logout:
                logOut();
                break;
        }
        return true;
    }
}