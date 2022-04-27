package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.changetheworld.model.FireStoreDB;

public class search_result_page extends AppCompatActivity implements RecycleSubWalletClickInterface{

    RecyclerView recyclerView;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_page);
        recyclerView = findViewById(R.id.SearchRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar);

        if(progressBar.getVisibility() != View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
        }
        String searchQuery = getIntent().getStringExtra("searchQuery");
        String radius = getIntent().getStringExtra("radius");
        radius = radius.substring(3);
        FireStoreDB.getInstance().searchChange(searchQuery, radius, recyclerView, this, progressBar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar = findViewById(R.id.progressBar);

        if(progressBar.getVisibility() != View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(int position) {
        /*Intent intent = new Intent(this,SubWallet.class);
        intent.putExtra("subWalletName",items.get(position).getCurrency());
        intent.putExtra(getString(R.string.userName), userName);
        intent.putExtra("userType", userType);
        intent.putExtra("localCurrencySymbol",symbol.getText().toString());
        intent.putExtra("subWalletBalance",items.get(position).getBalance());
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);*/
    }
}