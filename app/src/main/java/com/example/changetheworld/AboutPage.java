package com.example.changetheworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class AboutPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    String user_name;
    String userType;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);

        user_name = getIntent().getStringExtra(getString(R.string.userName));
        userType =  getIntent().getStringExtra("user_type");
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

    public void openClientProfile(){

        Intent intent = new Intent(this,ClientProfileActivity.class);
        intent.putExtra(getString(R.string.userName), user_name);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
    public void openBusinessProfile(){

        Intent intent = new Intent(this,BusinessProfileActivity.class);
        intent.putExtra(getString(R.string.userName), user_name);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void openCleintOrders(){
        Intent intent = new Intent(this,OrdersActivity.class);
        intent.putExtra(getString(R.string.userName), user_name);
        intent.putExtra("user_type", "PrivateClient");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void openBusinessOrders(){
        Intent intent = new Intent(this,BusinessOrdersActivity.class);
        intent.putExtra(getString(R.string.userName), user_name);
        intent.putExtra("user_type", "BusinessClient");
        startActivity(intent);
    }

    public void logOut(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void openClientHomePage(){
        Intent intent = new Intent(this, client_home_page.class);
        intent.putExtra(getString(R.string.userName),user_name);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void openBusinessHomePage(){
        Intent intent = new Intent(this, BusinessHomePage.class);
        intent.putExtra(getString(R.string.userName),user_name);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    private void openBusinessRates() {
        Intent intent = new Intent(this,Bussiness_rates.class);
        intent.putExtra(getString(R.string.userName), user_name);
        startActivity(intent);
    }

    public void openClientWallet(){

        Intent intent = new Intent(this,WalletActivity.class);
        intent.putExtra(getString(R.string.userName), user_name);
        intent.putExtra("user_type", "PrivateClient");
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void openBusinessWallet(){

        Intent intent = new Intent(this,WalletActivity.class);
        intent.putExtra(getString(R.string.userName), user_name);
        intent.putExtra("user_type", "BusinessClient");
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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
                if(userType.equals("PrivateClient")){
                    openClientWallet();
                }
                else {
                    openBusinessWallet();
                }
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
                drawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }
}



