package com.example.changetheworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.Order;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class BusinessOrdersActivity extends AppCompatActivity implements RecycleSubWalletClickInterface,NavigationView.OnNavigationItemSelectedListener {

    RecyclerView orders_RV;
    TextView TVPannding,TVCancle,TVApprove,TVComplete;
    String user_name;
    String user_type;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ArrayList<Order> pendding_items, canceled_items, approve_items, complete_items;
    Button orders_as_client, orders_as_business;
    String listClicked = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_orders);


        user_name = getIntent().getStringExtra(getString(R.string.userName));
        user_type = getIntent().getStringExtra("user_type");

        orders_RV = findViewById(R.id.orders_RV);
        orders_RV.setLayoutManager(new LinearLayoutManager(this));

        orders_as_client = findViewById(R.id.my_orders_btn);
        orders_as_business = findViewById(R.id.clients_orders_btn);

        TVPannding=findViewById(R.id.TvPennding);
        TVCancle=findViewById(R.id.TvCancle);
        TVApprove=findViewById(R.id.TvApprove);
        TVComplete=findViewById(R.id.TvComplete);

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

    public void MyOrders(){
        TVPannding.setText("");
        TVCancle.setVisibility(View.INVISIBLE);
        TVApprove.setVisibility(View.INVISIBLE);
        TVComplete.setVisibility(View.INVISIBLE);
        orders_RV.setVisibility(View.INVISIBLE);
    }

    public void businessOrder(){
            TVPannding.setText(R.string.pannding);
            if (listClicked == "")
                listClicked = "pending";
            TVCancle.setVisibility(View.VISIBLE);
            TVApprove.setVisibility(View.VISIBLE);
            TVComplete.setVisibility(View.VISIBLE);
            orders_RV.setVisibility(View.VISIBLE);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onResume() {
        super.onResume();
        businessOrder();
        pendding_items = new ArrayList<>();
        canceled_items = new ArrayList<>();
        approve_items = new ArrayList<>();
        complete_items = new ArrayList<>();

        FireStoreDB.getInstance().loadOrdersAsBusiness(this, user_name, user_type, pendding_items, canceled_items, approve_items, complete_items, orders_RV, listClicked);

            TVPannding.setOnClickListener(view -> {
                listClicked = "pending";
                TVPannding.setTextColor(Color.BLUE);
                TVCancle.setTextColor(Color.BLACK);
                TVComplete.setTextColor(Color.BLACK);
                TVApprove.setTextColor(Color.BLACK);
                FireStoreDB.getInstance().loadOrdersAsBusiness(this, user_name, user_type, pendding_items, canceled_items, approve_items, complete_items, orders_RV, listClicked);

            });

            TVCancle.setOnClickListener(view -> {
                listClicked = "canceled";
                TVCancle.setTextColor(Color.BLUE);
                TVPannding.setTextColor(Color.BLACK);
                TVComplete.setTextColor(Color.BLACK);
                TVApprove.setTextColor(Color.BLACK);
                FireStoreDB.getInstance().loadOrdersAsBusiness(this, user_name, user_type, pendding_items, canceled_items, approve_items, complete_items, orders_RV, listClicked);

            });

            TVApprove.setOnClickListener(view -> {
                listClicked = "approve";
                TVApprove.setTextColor(Color.BLUE);
                TVPannding.setTextColor(Color.BLACK);
                TVComplete.setTextColor(Color.BLACK);
                TVCancle.setTextColor(Color.BLACK);
                FireStoreDB.getInstance().loadOrdersAsBusiness(this, user_name, user_type, pendding_items, canceled_items, approve_items, complete_items, orders_RV, listClicked);

            });

            TVComplete.setOnClickListener(view -> {
                listClicked = "complete";
                TVComplete.setTextColor(Color.BLUE);
                TVPannding.setTextColor(Color.BLACK);
                TVApprove.setTextColor(Color.BLACK);
                TVCancle.setTextColor(Color.BLACK);
                FireStoreDB.getInstance().loadOrdersAsBusiness(this, user_name, user_type, pendding_items, canceled_items, approve_items, complete_items, orders_RV, listClicked);

            });

        orders_as_client.setOnClickListener(view -> {
            MyOrders();
            pendding_items = new ArrayList<>();
            FireStoreDB.getInstance().loadOrdersAsClient(this, user_name, user_type, pendding_items, orders_RV);
        });
        orders_as_business.setOnClickListener(view -> {
            businessOrder();
            listClicked = "pending";
            pendding_items = new ArrayList<>();
            canceled_items = new ArrayList<>();
            approve_items = new ArrayList<>();
            complete_items = new ArrayList<>();
            FireStoreDB.getInstance().loadOrdersAsBusiness(this, user_name, user_type, pendding_items, canceled_items, approve_items, complete_items, orders_RV, listClicked);
        });



    }

    @Override
    public void onItemClick(int position, String recycle_id) {
        if (TVPannding.getText().toString().equals(R.string.my_Orders)) {
            Intent intent = new Intent(this, OrderConfirm.class);
            intent.putExtra("user_type",user_type);
            intent.putExtra("user_name",user_name);
            intent.putExtra("orderID", pendding_items.get(position).getId());
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, OrderDetails.class);
            intent.putExtra("user_name",user_name);
            if (recycle_id.equals("pending")) {
                intent.putExtra("orderID", pendding_items.get(position).getId());
            }
            if (recycle_id.equals("canceled")) {
                intent.putExtra("orderID", canceled_items.get(position).getId());
            }
            if (recycle_id.equals("approve")) {
                intent.putExtra("orderID", approve_items.get(position).getId());
            }
            if (recycle_id.equals("complete")) {
                intent.putExtra("orderID", complete_items.get(position).getId());
            }
            startActivity(intent);
        }
    }

    public void openWallet(){

        Intent intent = new Intent(this,WalletActivity.class);
        intent.putExtra(getString(R.string.userName), user_name);
        intent.putExtra("user_type", "BusinessClient");
        startActivity(intent);
    }

    public void openBusinessHomepage(){

        Intent intent = new Intent(this,BusinessHomePage.class);
        intent.putExtra(getString(R.string.userName), user_name);
        startActivity(intent);
    }
    public void openAbout(){
        Intent intent = new Intent(this,AboutPage.class);
        intent.putExtra(getString(R.string.userName), user_name);
        intent.putExtra("user_type", "BusinessClient");
        startActivity(intent);
    }

    private void openBusinessRates() {
        Intent intent = new Intent(this,Bussiness_rates.class);
        intent.putExtra(getString(R.string.userName), user_name);
        startActivity(intent);
    }

    public void openBusinessProfile(){

        Intent intent = new Intent(this,BusinessProfileActivity.class);
        intent.putExtra(getString(R.string.userName), user_name);
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
                openBusinessRates();
                break;
            case R.id.nav_orders:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_logout:
                logOut();
                break;
            case R.id.nav_about:
                openAbout();
                break;
        }
        return true;
    }
}