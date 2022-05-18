package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.Order;

import java.util.ArrayList;

public class BusinessOrdersActivity extends AppCompatActivity implements RecycleSubWalletClickInterface {

    RecyclerView PanddingrecyclerView;
    RecyclerView CanclerecyclerView;
    RecyclerView ApproverecyclerView;
    RecyclerView CompleterecyclerView;
    TextView TVPannding,TVCancle,TVApprove,TVComplete;
    String user_name;
    String user_type;
    ArrayList<Order> pendding_items, canceled_items, approve_items, complete_items;
    Button orders_as_client, orders_as_business;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_orders);


        user_name = getIntent().getStringExtra(getString(R.string.userName));
        user_type = getIntent().getStringExtra("user_type");

        PanddingrecyclerView = findViewById(R.id.RVPanddingRecycle);
        PanddingrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        CanclerecyclerView = findViewById(R.id.RVCancleRecycle);
        CanclerecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ApproverecyclerView = findViewById(R.id.RVApproveRecycle);
        ApproverecyclerView.setLayoutManager(new LinearLayoutManager(this));
        CompleterecyclerView = findViewById(R.id.RVCompleteRecycle);
        CompleterecyclerView.setLayoutManager(new LinearLayoutManager(this));

        orders_as_client = findViewById(R.id.my_orders_btn);
        orders_as_business = findViewById(R.id.clients_orders_btn);

        TVPannding=findViewById(R.id.TvPennding);
        TVCancle=findViewById(R.id.TvCancle);
        TVApprove=findViewById(R.id.TvApprove);
        TVComplete=findViewById(R.id.TvComplete);

    }

    public void MyOrders(){
        TVPannding.setText(R.string.my_Orders);
        TVCancle.setVisibility(View.INVISIBLE);
        TVApprove.setVisibility(View.INVISIBLE);
        TVComplete.setVisibility(View.INVISIBLE);
        CanclerecyclerView.setVisibility(View.INVISIBLE);
        ApproverecyclerView.setVisibility(View.INVISIBLE);
        CompleterecyclerView.setVisibility(View.INVISIBLE);
    }

    public void businessOrder(){
            TVPannding.setText(R.string.pannding);
            TVCancle.setVisibility(View.VISIBLE);
            TVApprove.setVisibility(View.VISIBLE);
            TVComplete.setVisibility(View.VISIBLE);
            CanclerecyclerView.setVisibility(View.VISIBLE);
            ApproverecyclerView.setVisibility(View.VISIBLE);
            CompleterecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        businessOrder();
        pendding_items = new ArrayList<>();
        canceled_items = new ArrayList<>();
        approve_items = new ArrayList<>();
        complete_items = new ArrayList<>();
        FireStoreDB.getInstance().loadOrdersAsBusiness(this, user_name, user_type, pendding_items, canceled_items, approve_items, complete_items, PanddingrecyclerView, CanclerecyclerView, ApproverecyclerView, CompleterecyclerView);

        orders_as_client.setOnClickListener(view -> {
            MyOrders();
            pendding_items = new ArrayList<>();
            FireStoreDB.getInstance().loadOrdersAsClient(this, user_name, user_type, pendding_items, PanddingrecyclerView);
        });
        orders_as_business.setOnClickListener(view -> {
            businessOrder();
            pendding_items = new ArrayList<>();
            canceled_items = new ArrayList<>();
            approve_items = new ArrayList<>();
            complete_items = new ArrayList<>();
            FireStoreDB.getInstance().loadOrdersAsBusiness(this, user_name, user_type, pendding_items, canceled_items, approve_items, complete_items, PanddingrecyclerView, CanclerecyclerView, ApproverecyclerView, CompleterecyclerView);
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, OrderConfirm.class);
        intent.putExtra("user_type",user_type);
        intent.putExtra("user_name",user_name);
//        intent.putExtra("orderID", items.get(position).getId());
        startActivity(intent);
    }

}