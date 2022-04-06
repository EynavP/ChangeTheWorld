package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.PrivateClient;

public class SubWallet extends AppCompatActivity {

    private Button deposit;
    private Button withdraw;
    RecyclerView recyclerView;
    TextView subWalletName_TV;
    String userName;
    String userType;
    String subWalletName;
    String localCurrencySymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_wallet);
        deposit=(Button) findViewById(R.id.DepositTitle);
        withdraw=(Button) findViewById(R.id.withdrawTitle);
        recyclerView = findViewById(R.id.SubWalletRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        subWalletName = getIntent().getStringExtra("subWalletName");
        subWalletName_TV = findViewById(R.id.subwalletName);
        subWalletName_TV.setText(subWalletName);
        userName = getIntent().getStringExtra(getString(R.string.userName));
        userType = getIntent().getStringExtra("userType");
        localCurrencySymbol = getIntent().getStringExtra("localCurrencySymbol");

        deposit.setOnClickListener(view -> {
            moveToDeposite();
        });
        withdraw.setOnClickListener(view -> {
            //FireStoreDB.getInstance().updateBalance(userName, userType,subWalletName,50,"-",this);
            moveToWithdraw();
        });




    }

    @Override
    protected void onResume() {
        super.onResume();
        FireStoreDB.getInstance().loadWalletHistory(userName, userType, subWalletName, recyclerView, this);
    }

    public void moveToDeposite(){
        Intent intent = new Intent(this,DepositPage.class);
        intent.putExtra("subWalletName",subWalletName);
        intent.putExtra(getString(R.string.userName), userName);
        intent.putExtra("userType", userType);
        intent.putExtra("localCurrencySymbol", localCurrencySymbol);

        startActivity(intent);
    }
    public void moveToWithdraw(){
        Intent intent = new Intent(this,WithdrawPage.class);
        intent.putExtra("subWalletName",subWalletName);
        intent.putExtra(getString(R.string.userName), userName);
        intent.putExtra("userType", userType);
        intent.putExtra("localCurrencySymbol", localCurrencySymbol);
        startActivity(intent);
    }

}