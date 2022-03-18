package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.changetheworld.model.Wallet;

import java.util.ArrayList;

public class WalletActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterWallet adapterWallet;
    ArrayList<Wallet> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        items = new ArrayList<Wallet>();
        Wallet wallet = new Wallet(100, "DOLLAR", "niv", "$", 130, "Euro");
        items.add(wallet);

        recyclerView=findViewById(R.id.recycleWallet);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterWallet = new AdapterWallet(this,items);
        recyclerView.setAdapter(adapterWallet);

    }
}