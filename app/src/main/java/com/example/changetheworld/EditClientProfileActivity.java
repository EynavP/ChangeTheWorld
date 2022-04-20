package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.PrivateClient;

public class EditClientProfileActivity extends AppCompatActivity {

    String userName;
    EditText full_name;
    EditText mail_address;
    EditText phone_number;
    EditText password;
    Spinner local_currency;
    ImageView profile;
    Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client_profile);

        local_currency = findViewById(R.id.localCurrency_value);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        adapter.addAll(FireStoreDB.getInstance().currenciesToSymbol.keySet());
        local_currency.setAdapter(adapter);

        full_name = findViewById(R.id.fullname_value);
        mail_address = findViewById(R.id.maillAddress_value);
        phone_number = findViewById(R.id.phoneNumber_value);
        password = findViewById(R.id.password_value);
        userName = getIntent().getStringExtra("user_name");
        ((TextView)findViewById(R.id.username_profile_name)).setText(userName);
        FireStoreDB.getInstance().loadClientDataForEdit(userName, full_name, mail_address, phone_number , local_currency, password);
        profile = findViewById(R.id.profilePhoto);
        FireStoreDB.getInstance().LoadProfilePhoto(profile, userName);

        updateButton = findViewById(R.id.updateBtn);
        updateButton.setOnClickListener(view -> {
            String new_full_name = full_name.getText().toString();
            String new_mail_address = mail_address.getText().toString();
            String new_phone_number = phone_number.getText().toString();
            String new_local_currency = local_currency.getSelectedItem().toString();
            String new_password = password.getText().toString();

            if (new_full_name.isEmpty() || !new_full_name.matches("[a-zA-z\\s]*$")){
                Toast toast = Toast.makeText(this, getString(R.string.Invalid_full_name), Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (new_mail_address.isEmpty() || !new_mail_address.matches("^(.+)@(\\S+)$")){
                Toast toast = Toast.makeText(this, getString(R.string.Invalid_mail), Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (new_phone_number.isEmpty() || !new_phone_number.matches("^[0-9]*$")){
                Toast toast = Toast.makeText(this, getString(R.string.Invalid_phone_number), Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (new_local_currency.isEmpty() || new_local_currency.equals(getString(R.string.choose_Currency))){
                Toast toast = Toast.makeText(this, getString(R.string.Invalid_currency), Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (new_password.isEmpty() || !new_password.matches("^[A-Za-z0-9]*$")){
                Toast toast = Toast.makeText(this, getString(R.string.password_invalid), Toast.LENGTH_SHORT);
                toast.show();
            }else{
                PrivateClient client = new PrivateClient(userName,new_full_name,new_mail_address,new_phone_number, new_local_currency, new_password,null,null);
                Intent intent = new Intent(this, ClientProfileActivity.class);
                FireStoreDB.getInstance().updateClientProfile(this, client, intent);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        FireStoreDB.getInstance().loadClientDataForEdit(userName, full_name, mail_address, phone_number , local_currency, password);
        profile = findViewById(R.id.profilePhoto);
        FireStoreDB.getInstance().LoadProfilePhoto(profile, userName);
    }
}