package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.changetheworld.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private Button createAccount;
    private Button createBusinessAccount;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        createAccount = (Button) findViewById(R.id.createClientAccount);
        createBusinessAccount = (Button) findViewById(R.id.createBusinessAccount);
        createAccount.setOnClickListener(view ->  { openCreateNewAccount(); });
        createBusinessAccount.setOnClickListener(view -> {openCreateBusinessAccount(); });

        binding.login.setOnClickListener(view->{
            String userName = binding.editTextTextPersonName.getText().toString();
            String password = binding.editTextTextPassword.getText().toString();
            if (userName.isEmpty()){
                Toast toast = Toast.makeText(this, "username invalid, please try Again",Toast.LENGTH_LONG);
                toast.show();
            }
            if (password.isEmpty()){
                Toast toast = Toast.makeText(this, "password invalid, please try Again",Toast.LENGTH_LONG);
                toast.show();
            }
            else
                openClientHomePage();

        });
    }

    public void openCreateNewAccount(){
        Intent intent = new Intent(this,CreateAccountPrivate.class);
        startActivity(intent);
    }

    public void openCreateBusinessAccount(){
        Intent intent = new Intent(this,CreateBusinessAccount.class);
        startActivity(intent);
    }

    public void openClientHomePage(){
        Intent intent = new Intent(this,ClientHomePage.class);
        startActivity(intent);
    }





}