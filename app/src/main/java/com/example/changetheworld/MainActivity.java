package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button createAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createAccount = (Button) findViewById(R.id.createAccount);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateNewAccount();
            }
        });
    }

    public void openCreateNewAccount(){
        Intent intent = new Intent(this,CreateAccountPrivate.class);
        startActivity(intent);
    }
}