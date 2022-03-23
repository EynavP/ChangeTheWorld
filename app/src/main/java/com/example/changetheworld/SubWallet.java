package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SubWallet extends AppCompatActivity {

    private ImageView deposit;
    private ImageView withdraw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_wallet);
        deposit=(ImageView) findViewById(R.id.depositeClick);
        withdraw=(ImageView) findViewById(R.id.widthdrawClick);

        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToDeposite();
            }
        });

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToWithdraw();
            }
        });

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