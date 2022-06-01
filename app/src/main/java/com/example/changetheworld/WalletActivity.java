package com.example.changetheworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.Wallet;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import android.app.*;


public class WalletActivity extends AppCompatActivity implements RecycleSubWalletClickInterface, NavigationView.OnNavigationItemSelectedListener {


    RecyclerView recyclerView;
    ArrayList<Wallet> items;
    String userName;
    String userType;
    TextView totalBalance;
    TextView symbol;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        userName = getIntent().getStringExtra(getString(R.string.userName));
        userType =  getIntent().getStringExtra("user_type");


        recyclerView= findViewById(R.id.recycleWallet);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        totalBalance = findViewById(R.id.balance);
        symbol = findViewById(R.id.symbol);

        drawerLayout = findViewById(R.id.drawer_menu);
        navigationView = findViewById(R.id.nav_view);
        toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if (userType.equals("BusinessClient")) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.open_menu_businees);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        items = new ArrayList<Wallet>();
        if (userType.equals("BusinessClient")) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.open_menu_businees);
        }
        FireStoreDB.getInstance().LoadWallets(this, userName, userType, items, recyclerView, totalBalance, symbol);
    }


    @Override
    public void onItemClick(int position, String recycle_id) {
        Intent intent = new Intent(this,SubWallet.class);
        intent.putExtra("subWalletName",items.get(position).getCurrency());
        intent.putExtra(getString(R.string.userName), userName);
        intent.putExtra("userType", userType);
        intent.putExtra("localCurrencySymbol",symbol.getText().toString());
        intent.putExtra("subWalletBalance",items.get(position).getBalance());
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void openClientProfile(){

        Intent intent = new Intent(this,ClientProfileActivity.class);
        intent.putExtra(getString(R.string.userName), userName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
    public void openBusinessProfile(){

        Intent intent = new Intent(this,BusinessProfileActivity.class);
        intent.putExtra(getString(R.string.userName), userName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void openCleintOrders(){
        Intent intent = new Intent(this,OrdersActivity.class);
        intent.putExtra(getString(R.string.userName), userName);
        intent.putExtra("user_type", "PrivateClient");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void openBusinessOrders(){
        Intent intent = new Intent(this,BusinessOrdersActivity.class);
        intent.putExtra(getString(R.string.userName), userName);
        intent.putExtra("user_type", "BusinessClient");
        startActivity(intent);
    }

    public void logOut(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void openClientHomePage(){
        Intent intent = new Intent(this, client_home_page.class);
        intent.putExtra(getString(R.string.userName),userName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void openBusinessHomePage(){
        Intent intent = new Intent(this, BusinessHomePage.class);
        intent.putExtra(getString(R.string.userName),userName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    private void openBusinessRates() {
        Intent intent = new Intent(this,Bussiness_rates.class);
        intent.putExtra(getString(R.string.userName), userName);
        startActivity(intent);
    }

    public void openBusinessAbout(){
        Intent intent = new Intent(this,AboutPage.class);
        intent.putExtra(getString(R.string.userName), userName);
        intent.putExtra("user_type", "BusinessClient");
        startActivity(intent);
    }

    public void openClientAbout(){
        Intent intent = new Intent(this,AboutPage.class);
        intent.putExtra(getString(R.string.userName), userName);
        intent.putExtra("user_type", "PrivateClient");
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.nav_home:
                if(userType.equals("PrivateClient")){
                    openClientHomePage();
                }
                else {
                   openBusinessHomePage();
                }
                break;
            case R.id.nav_wallet:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_profile:
                if(userType.equals("PrivateClient")){
                    openClientProfile();
                }
                else {
                    openBusinessProfile();
                }
                break;
            case R.id.nav_orders:
                if(userType.equals("PrivateClient")){
                    openCleintOrders();
                }
                else {
                    openBusinessOrders();
                }
                break;
            case R.id.nav_logout:
                logOut();
                break;
            case R.id.nav_update_rates:
                openBusinessRates();
                break;
            case R.id.nav_about:
                if(userType.equals("PrivateClient")){
                    openClientAbout();
                }
                else {
                    openBusinessAbout();
                }
        }
        return true;
    }
}