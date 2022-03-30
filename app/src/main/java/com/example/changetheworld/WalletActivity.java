package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.Wallet;

import java.util.ArrayList;

public class WalletActivity extends AppCompatActivity implements RecycleSubWalletClickInterface {

    RecyclerView recyclerView;
    ArrayList<Wallet> items;
    String userName;
    TextView user_nameTextView;
    TextView totalBalance;
    TextView symbol;
    Button gotoSubWallet;
    AdapterWallet adapterWallet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        userName = getIntent().getStringExtra(getString(R.string.userName));
        user_nameTextView = findViewById(R.id.user_name);
        user_nameTextView.setText(userName + "'s wallet");

        recyclerView= findViewById(R.id.recycleWallet);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        totalBalance = findViewById(R.id.balance);
        symbol = findViewById(R.id.symbol);

        gotoSubWallet = (Button)findViewById(R.id.gotoSubWallet);
        gotoSubWallet.setOnClickListener(view->{gotoSubWalletFunc();});

    }

    @Override
    protected void onResume() {
        super.onResume();
        items = new ArrayList<Wallet>();

        FireStoreDB.getInstance().LoadWallets(this, userName, getString(R.string.PrivateClient), items, recyclerView, totalBalance, symbol);
    }

    public void gotoSubWalletFunc(){
        Intent intent = new Intent(this,SubWallet.class);
        startActivity(intent);
    }



    @Override
    public void onItemClick(View view, int position) {

    }
}