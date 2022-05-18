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

import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.Order;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity implements RecycleSubWalletClickInterface, NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    String user_name;
    String user_type;
    ArrayList<Order> items;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        user_name = getIntent().getStringExtra(getString(R.string.userName));
        user_type = getIntent().getStringExtra("user_type");
        recyclerView = findViewById(R.id.ordersRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

        items = new ArrayList<>();
        FireStoreDB.getInstance().loadOrdersAsClient(this, user_name, user_type, items, recyclerView);
    }

    @Override
    public void onItemClick(int position, String recycle_id) {
        Intent intent = new Intent(this, OrderConfirm.class);
        intent.putExtra("user_type",user_type);
        intent.putExtra("user_name",user_name);
        intent.putExtra("orderID", items.get(position).getId());
        startActivity(intent);
    }

    public void openWallet(){

        Intent intent = new Intent(this,WalletActivity.class);
        intent.putExtra(getString(R.string.userName), user_name);
        intent.putExtra("user_type", "PrivateClient");
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void openProfile(){

        Intent intent = new Intent(this,ClientProfileActivity.class);
        intent.putExtra(getString(R.string.userName), user_name);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void logOut(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.nav_home:
                Intent intent = new Intent(this, client_home_page.class);
                intent.putExtra(getString(R.string.userName),user_name);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                break;
            case R.id.nav_wallet:
                openWallet();
                break;
            case R.id.nav_profile:
                openProfile();
                break;
            case R.id.nav_orders:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_logout:
                logOut();
                break;
        }
        return true;
    }
}