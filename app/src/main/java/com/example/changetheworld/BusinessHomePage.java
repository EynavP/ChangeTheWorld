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
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changetheworld.model.AutoCompleteApi;
import com.example.changetheworld.model.AutoCompleteInterface;
import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.Order;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

public class BusinessHomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView recyclerView;
    ArrayList<Order> items;
    AdapterOrder adapter;
    String user_name, user_type;
    AutoCompleteTextView autoCompleteTextView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    AutoCompleteInterface aci = new AutoCompleteApi();
    SeekBar bar;
    TextView bar_text, dayDate, todayDate, userName, orders_for_today, new_orders, cash_orders;
    ImageView search;

    @Override
    protected void onResume() {
        super.onResume();
        FireStoreDB.getInstance().LoadOrdersStatus(this, orders_for_today, new_orders, cash_orders, user_type, user_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_home_page);
        orders_for_today = findViewById(R.id.orders_FT_num);
        new_orders = findViewById(R.id.new_orders_num);
        cash_orders = findViewById(R.id.cash_order_num);
        userName = findViewById(R.id.username);
        userName.setText(user_name);
        user_name = getIntent().getStringExtra(getString(R.string.userName));
        user_type = "BusinessClient";




        drawerLayout = findViewById(R.id.drawer_menu_business);
        navigationView = findViewById(R.id.nav_view);
        toolbar =(Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

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
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(BusinessHomePage.this, android.R.layout.simple_dropdown_item_1line, result);
                            autoCompleteTextView.setAdapter(adapter);
                        });
                    });
                    t.start();
                }
            }
        });
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
                intent.putExtra("user_type", "BusinessClient");
                startActivity(intent);
            }
        });

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        dayDate = findViewById(R.id.dayDate);
        dayDate.setText(formatter.format(date));
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        todayDate = findViewById(R.id.todayDate);
        todayDate.setText(formatter.format(date));


    }

    public void openWallet(){

        Intent intent = new Intent(this,WalletActivity.class);
        intent.putExtra(getString(R.string.userName), user_name);
        intent.putExtra("user_type", "BusinessClient");
        startActivity(intent);
    }

    public void openBusinessProfile(){

        Intent intent = new Intent(this,BusinessProfileActivity.class);
        intent.putExtra(getString(R.string.userName), user_name);
        startActivity(intent);
    }

    private void openBusinessRates() {
        Intent intent = new Intent(this,Bussiness_rates.class);
        intent.putExtra(getString(R.string.userName), user_name);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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