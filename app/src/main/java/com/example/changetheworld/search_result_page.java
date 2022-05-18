package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.Search;

import java.util.ArrayList;
import java.util.List;

public class search_result_page extends AppCompatActivity implements RecycleSubWalletClickInterface{

    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<Search> filter_list;
    String user_name;
    String user_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_page);
        recyclerView = findViewById(R.id.SearchRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar);
        filter_list = new ArrayList<>();
        user_name = getIntent().getStringExtra(getString(R.string.client_user_name));
        user_type = getIntent().getStringExtra("user_type");
        if(progressBar.getVisibility() != View.INVISIBLE) {
            progressBar.setVisibility(View.VISIBLE);
        }
        String searchQuery = getIntent().getStringExtra("searchQuery");
        String radius = getIntent().getStringExtra("radius");
        radius = radius.substring(3);
        FireStoreDB.getInstance().searchChange(searchQuery, radius, recyclerView, this, progressBar, filter_list);
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
    public void onItemClick(int position, String recycle_id) {
        Intent intent = new Intent(this,BusinessPage.class);
        intent.putExtra(getString(R.string.business_user_name), filter_list.get(position).getUserName());
        intent.putExtra(getString(R.string.client_user_name), user_name);
        intent.putExtra("user_type", user_type);
        startActivity(intent);
    }
}