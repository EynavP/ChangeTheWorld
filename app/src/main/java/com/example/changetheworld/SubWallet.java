package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.PrivateClient;

public class SubWallet extends AppCompatActivity {

    private Button deposit;
    private Button withdraw;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_wallet);
        deposit=(Button) findViewById(R.id.DepositTitle);
        withdraw=(Button) findViewById(R.id.withdrawTitle);
        recyclerView = findViewById(R.id.SubWalletRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        deposit.setOnClickListener(view -> {
            FireStoreDB.getInstance().updateBalance("yuval", "PrivateClient", "ILS", 20, "+", this);
        });
        withdraw.setOnClickListener(view -> {
            FireStoreDB.getInstance().updateBalance("yuval", getString(R.string.PrivateClient),getString(R.string.ILS),50,"-",this);
        });

        FireStoreDB.getInstance().loadWalletHistory("yuval", "PrivateClient", "ILS", recyclerView, this);
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