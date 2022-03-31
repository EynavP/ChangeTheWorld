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
    String subWalletName;

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

        deposit.setOnClickListener(view -> {
            FireStoreDB.getInstance().updateBalance(userName, "PrivateClient", subWalletName, 20, "+", this);
        });
        withdraw.setOnClickListener(view -> {
            FireStoreDB.getInstance().updateBalance(userName, getString(R.string.PrivateClient),subWalletName,50,"-",this);
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        FireStoreDB.getInstance().loadWalletHistory(userName, "PrivateClient", subWalletName, recyclerView, this);
    }

    public void moveToDeposite(){
        Intent intent = new Intent(this,client_home_page.class);
        startActivity(intent);
    }
    public void moveToWithdraw(){
        Intent intent = new Intent(this,client_home_page.class);
        startActivity(intent);
    }

}