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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.changetheworld.model.FireStoreDB;
import com.google.android.material.navigation.NavigationView;

public class BusinessProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String userName;
    TextView business_name;
    TextView mail_address;
    TextView phone_number;
    TextView owner_name;
    TextView header;
    TextView local_currency;
    TextView address;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    TextView sundayHours, monThuHours, fridayHours, saturdayHours;

    Button editBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_profile);

        business_name = findViewById(R.id.business_name_value);
        mail_address = findViewById(R.id.maillAddress_value);
        phone_number = findViewById(R.id.phoneNumber_value);
        owner_name = findViewById(R.id.business_owner_name_value);
        address = findViewById(R.id.state_value);
        local_currency = findViewById(R.id.local_currency_value);

        sundayHours = findViewById(R.id.sundayHours);
        monThuHours = findViewById(R.id.monThuHours);
        fridayHours = findViewById(R.id.fridayHours);
        saturdayHours = findViewById(R.id.saturdayHours);

        userName = getIntent().getStringExtra(getString(R.string.userName));
        ((TextView)findViewById(R.id.business_username_profile_name)).setText(userName);
        editBtn = findViewById(R.id.editBtn);
        editBtn.setOnClickListener(view -> {OpenEditProfileBusiness();});

        drawerLayout = findViewById(R.id.drawer_menu_business);
        navigationView = findViewById(R.id.nav_view);
        toolbar =(Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void OpenEditProfileBusiness() {
        Intent intent = new Intent(this,EditBusinessProfileActivity.class);
        intent.putExtra("user_name", userName);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FireStoreDB.getInstance().loadBusinessData(userName, header, business_name, mail_address, phone_number, owner_name, address, local_currency, sundayHours, monThuHours, fridayHours, saturdayHours);

    }

    public void openWallet(){

        Intent intent = new Intent(this,WalletActivity.class);
        intent.putExtra(getString(R.string.userName), userName);
        intent.putExtra("user_type", "BusinessClient");
        startActivity(intent);
    }

    public void openBusinessHomepage(){

        Intent intent = new Intent(this,BusinessHomePage.class);
        intent.putExtra(getString(R.string.userName), userName);
        startActivity(intent);
    }

    private void openBusinessRates() {
        Intent intent = new Intent(this,Bussiness_rates.class);
        intent.putExtra(getString(R.string.userName), userName);
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
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_update_rates:
                openBusinessRates();
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