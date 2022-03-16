package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.currency;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class client_home_page extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<currency> items;
    TextView userName;
    ImageView profilPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home_page);

        userName = findViewById(R.id.username);
        String user_name = getIntent().getStringExtra("userName");
        userName.setText(user_name);



        profilPhoto = findViewById(R.id.profilePhoto);

        FireStoreDB.getInstance().LoadProfilePhoto(profilPhoto, user_name);



        items = new ArrayList<>();
        items.add(new currency(R.drawable.dollar,"3.6","0.30"));
        items.add(new currency(R.drawable.euro,"3.5","-0.35"));
        recyclerView=findViewById(R.id.recycle_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this,items);
        recyclerView.setAdapter(adapter);
    }
}