package com.example.changetheworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.changetheworld.model.FireStoreDB;
import com.google.android.material.navigation.NavigationView;

public class BusinessPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String userName, client_user_name, user_type;
    TextView business_name;
    TextView mail_address;
    TextView phone_number;
    TextView owner_name;
    TextView header;
    Button address;
    TextView local_currency;
    DrawerLayout drawerLayout;

    TextView sundayHours, monThuHours, fridayHours, saturdayHours;

    Button newOrder, currencies_rates_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_page);


        business_name = findViewById(R.id.business_name_value);
        mail_address = findViewById(R.id.maillAddress_value);
        phone_number = findViewById(R.id.phoneNumber_value);
        header =  findViewById(R.id.business_username_profile_name);
        local_currency = findViewById(R.id.local_currency_value);
        address = findViewById(R.id.address_value);
        sundayHours = findViewById(R.id.sundayHours);
        monThuHours = findViewById(R.id.monThuHours);
        fridayHours = findViewById(R.id.fridayHours);
        saturdayHours = findViewById(R.id.saturdayHours);

        userName = getIntent().getStringExtra(getString(R.string.business_user_name));
        client_user_name = getIntent().getStringExtra(getString(R.string.client_user_name));
        user_type = getIntent().getStringExtra("user_type");
        FireStoreDB.getInstance().loadBusinessData(userName,header, business_name, mail_address, phone_number, owner_name, address, local_currency, sundayHours, monThuHours, fridayHours, saturdayHours);


        newOrder = findViewById(R.id.new_order_btn);
        newOrder.setOnClickListener(view -> {
            FireStoreDB.getInstance().checkExistPassport(this, userName, client_user_name, user_type);
        });

        currencies_rates_btn = findViewById(R.id.currencies_rates_btn);
        currencies_rates_btn.setOnClickListener(view -> {
            Intent intent = new Intent(this,Business_rate_for_show.class);
            intent.putExtra(getString(R.string.business_user_name), userName);
            intent.putExtra(getString(R.string.client_user_name), client_user_name);
            startActivity(intent);
        });

        address.setOnClickListener(view -> {
            FireStoreDB.getInstance().openOnMaps(this, address.getText().toString());
        });
    }

    public void openWallet(){

        Intent intent = new Intent(this,WalletActivity.class);
        intent.putExtra(getString(R.string.userName), userName);
        intent.putExtra("user_type", "BusinessClient");
        startActivity(intent);
    }

    public void openBusinessProfile(){

        Intent intent = new Intent(this,BusinessProfileActivity.class);
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
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_wallet:
                openWallet();
                break;
            case R.id.nav_profile:
                openBusinessProfile();
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