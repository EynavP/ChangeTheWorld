package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changetheworld.databinding.ActivityMainBinding;
import com.example.changetheworld.model.FireStoreDB;

public class MainActivity extends AppCompatActivity {
    private Button createAccount;
    private Button login;
    private Button gotoBusiness;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = (Button) findViewById(R.id.login);

        createAccount = (Button) findViewById(R.id.createClientAccount);
        createAccount.setOnClickListener(view ->  { openCreateNewAccount(); });
        gotoBusiness = (Button) findViewById(R.id.gotoLoginBusiness);
        gotoBusiness.setOnClickListener(view->{gotoLoginBusiness();});

        login.setOnClickListener(view->{
            String userName = ((EditText) findViewById(R.id.editTextTextPersonName)).getText().toString();
            String password = ((EditText) findViewById(R.id.editTextTextPassword)).getText().toString();
            if (userName.isEmpty()){
                Toast toast = Toast.makeText(this, "username invalid, please try Again",Toast.LENGTH_LONG);
                toast.show();
            }
            if (password.isEmpty()){
                Toast toast = Toast.makeText(this, "password invalid, please try Again",Toast.LENGTH_LONG);
                toast.show();
            }
            else{
                Intent intent = new Intent(this,client_home_page.class);
                FireStoreDB.getInstance().VerifyAndLogin(this, userName, password, intent, "PrivateClient");

            }
        });
    }

    public void openCreateNewAccount(){
        Intent intent = new Intent(this,CreateAccountPrivate.class);
        startActivity(intent);
    }

    public void gotoLoginBusiness(){
        Intent intent = new Intent(this,BusinessLogin.class);
        startActivity(intent);
    }

}