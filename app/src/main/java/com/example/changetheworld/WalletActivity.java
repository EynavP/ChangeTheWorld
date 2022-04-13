package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.Wallet;

import java.util.ArrayList;

public class WalletActivity extends AppCompatActivity implements RecycleSubWalletClickInterface{

    RecyclerView recyclerView;
    ArrayList<Wallet> items;
    String userName;
    String userType;
    TextView user_nameTextView;
    TextView totalBalance;
    TextView symbol;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        userName = getIntent().getStringExtra(getString(R.string.userName));
        userType =  getIntent().getStringExtra("user_type");
      //  user_nameTextView = findViewById(R.id.user_name);
     //   user_nameTextView.setText(userName + "'s wallet");

        recyclerView= findViewById(R.id.recycleWallet);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        totalBalance = findViewById(R.id.balance);
        symbol = findViewById(R.id.symbol);


    }

    @Override
    protected void onResume() {
        super.onResume();
        items = new ArrayList<Wallet>();

        FireStoreDB.getInstance().LoadWallets(this, userName, userType, items, recyclerView, totalBalance, symbol);
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this,SubWallet.class);
        intent.putExtra("subWalletName",items.get(position).getCurrency());
        intent.putExtra(getString(R.string.userName), userName);
        intent.putExtra("userType", userType);
        intent.putExtra("localCurrencySymbol",symbol.getText().toString());
        intent.putExtra("subWalletBalance",items.get(position).getBalance());
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}