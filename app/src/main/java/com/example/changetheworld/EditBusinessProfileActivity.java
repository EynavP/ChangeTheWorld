package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changetheworld.model.BusinessClient;
import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.PrivateClient;

public class EditBusinessProfileActivity extends AppCompatActivity {

    String userName;
    TextView business_name;
    TextView mail_address;
    TextView phone_number;
    TextView owner_name;
    Spinner state;
    TextView city;
    TextView street;
    TextView number;
    EditText password;

    Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_business_profile);

        state = findViewById(R.id.state_value);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        adapter.addAll(FireStoreDB.getInstance().stateToCurrency.keySet());
        state.setAdapter(adapter);

        business_name = findViewById(R.id.business_name_value);
        mail_address = findViewById(R.id.maillAddress_value);
        phone_number = findViewById(R.id.phoneNumber_value);
        owner_name = findViewById(R.id.business_owner_name_value);
        state = findViewById(R.id.state_value);
        city = findViewById(R.id.city_value);
        street = findViewById(R.id.street_value);
        number = findViewById(R.id.number_value);
        password = findViewById(R.id.password_value);

        userName = getIntent().getStringExtra("user_name");
        ((TextView)findViewById(R.id.business_username_profile_name)).setText(userName);
        FireStoreDB.getInstance().loadBusinessDataForEdit(userName, business_name, mail_address, phone_number , owner_name, state, city, street, number, password);

        updateButton = findViewById(R.id.updateBtn);
        updateButton.setOnClickListener(view -> {
            String new_business_name = business_name.getText().toString();
            String new_mail_address = mail_address.getText().toString();
            String new_phone_number = phone_number.getText().toString();
            String new_owner_name = owner_name.getText().toString();
            String new_state = state.getSelectedItem().toString();
            String new_city = city.getText().toString();
            String new_street = street.getText().toString();
            String new_number = number.getText().toString();
            String new_password = password.getText().toString();

            if (new_business_name.isEmpty()){
                Toast toast = Toast.makeText(this, getString(R.string.invalid_business_name), Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (new_mail_address.isEmpty() || !new_mail_address.matches("^(.+)@(\\S+)$")){
                Toast toast = Toast.makeText(this, getString(R.string.Invalid_mail), Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (new_state.isEmpty() || new_state.equals(getString(R.string.choose_state))){
                Toast toast = Toast.makeText(this, getString(R.string.Invalid_state), Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (new_city.isEmpty()){
                Toast toast = Toast.makeText(this, R.string.invalid_business_city, Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (new_street == null){
                Toast toast = Toast.makeText(this, R.string.invalid_business_street, Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (new_number == null) {
                Toast toast = Toast.makeText(this, R.string.invalid_business_number, Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (new_phone_number.isEmpty() || !new_phone_number.matches("^[0-9]*$")){
                Toast toast = Toast.makeText(this, getString(R.string.Invalid_phone_number), Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (new_password.isEmpty() || !new_password.matches("^[A-Za-z0-9]*$")){
                Toast toast = Toast.makeText(this, getString(R.string.password_invalid), Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (new_owner_name.isEmpty() || !new_owner_name.matches("[a-zA-z\\s]*$")){
                Toast toast = Toast.makeText(this, getString(R.string.invalid_business_name), Toast.LENGTH_SHORT);
                toast.show();
            }
//            else if (business_chosen_approvel == null){
//                Toast toast = Toast.makeText(this, R.string.Invalid_business_approval_document, Toast.LENGTH_SHORT);
//                toast.show();
//            }
//            else if (business_chosen_owner_id == null) {
//                Toast toast = Toast.makeText(this, R.string.Invalid_business_owner_id, Toast.LENGTH_SHORT);
//                toast.show();
//            }
            else {
                BusinessClient business_client = new BusinessClient(new_business_name,new_mail_address,new_phone_number,userName,new_password,new_owner_name,null, null,new_state,new_city, new_street, new_number);
                Intent intent = new Intent(this, BusinessProfileActivity.class);
                FireStoreDB.getInstance().updateBusinessProfile(this, business_client, intent);
            }
        });
    }
}