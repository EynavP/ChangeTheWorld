package com.example.changetheworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.Wallet;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;


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


    }

    @Override
    protected void onResume() {
        super.onResume();
        items = new ArrayList<Wallet>();

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

    public void openProfile(){

        Intent intent = new Intent(this,ClientProfileActivity.class);
        intent.putExtra(getString(R.string.userName), userName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void openOrders(){

        Intent intent = new Intent(this,OrdersActivity.class);
        intent.putExtra(getString(R.string.userName), userName);
        intent.putExtra("user_type", "PrivateClient");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void logOut(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.nav_home:
                if(userType.equals("PrivateClient")){
                    Intent intent = new Intent(this, client_home_page.class);
                    intent.putExtra(getString(R.string.userName),userName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(this, BusinessHomePage.class);
                    intent.putExtra(getString(R.string.userName),userName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
                break;
            case R.id.nav_wallet:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_profile:
                openProfile();
                break;
            case R.id.nav_orders:
                openOrders();
                break;
            case R.id.nav_logout:
                logOut();
                break;
        }
        return true;
    }
}