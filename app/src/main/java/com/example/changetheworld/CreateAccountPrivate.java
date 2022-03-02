package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.changetheworld.databinding.ActivityMainBinding;

public class CreateAccountPrivate extends AppCompatActivity {
    Button returnLogin;
    Spinner currencies;
    String[] currency = {"Choose Currency","DOLLAR","EURO"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_private);
        returnLogin = findViewById(R.id.moveToLogin);
        returnLogin.setOnClickListener(view ->  { returnLoginPage(); });
        currencies = (Spinner) findViewById(R.id.enterLocalCurrency);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        adapter.addAll(currency);
        currencies.setAdapter(adapter);

        Button CreateAccountPrivate = findViewById(R.id.createAccountButton);
        CreateAccountPrivate.setOnClickListener(view -> {
            String full_name = ((EditText) findViewById(R.id.enterFullName)).getText().toString();
            String mail = ((EditText) findViewById(R.id.enterMailAddress)).getText().toString();
            String phone = ((EditText) findViewById(R.id.enterPhoneNumber)).getText().toString();
            String currency = ((Spinner) findViewById(R.id.enterLocalCurrency)).getSelectedItem().toString();
            String user_name = ((EditText) findViewById(R.id.enterNewUserName)).getText().toString();
            String password = ((EditText) findViewById(R.id.enterNewPassword)).getText().toString();
            //String personal_photo = ((EditText) findViewById(R.id.enter)).toString();
           // String passport_photo = findViewById(R.id.uploadPassport).toString();
            if (full_name.isEmpty() || !full_name.matches("[a-zA-z//s]*")){
                Toast toast = Toast.makeText(this, "Invalid full name", Toast.LENGTH_LONG);
                toast.show();
            }
            if (mail.isEmpty() || !mail.matches("^(.+)@(\\S+)$")){
                Toast toast = Toast.makeText(this, "Invalid mail", Toast.LENGTH_LONG);
                toast.show();
            }
            if (phone.isEmpty() || !phone.matches("^[0-9]*$")){
                Toast toast = Toast.makeText(this, "Invalid phone number", Toast.LENGTH_LONG);
                toast.show();
            }
            if (currency.isEmpty() || currency.equals("Choose Currency")){
                Toast toast = Toast.makeText(this, "Invalid currency", Toast.LENGTH_LONG);
                toast.show();
            }
            if (user_name.isEmpty() || user_name.contains(" ")){
                Toast toast = Toast.makeText(this, "Invalid username", Toast.LENGTH_LONG);
                toast.show();
            }
            if (password.isEmpty() || !password.matches("^[A-Za-z0-9]*$")){
                Toast toast = Toast.makeText(this, "Invalid password", Toast.LENGTH_LONG);
                toast.show();
            }

        });

    }

    public void returnLoginPage(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }




}