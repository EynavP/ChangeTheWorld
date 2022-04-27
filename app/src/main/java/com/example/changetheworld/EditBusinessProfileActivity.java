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
import com.example.changetheworld.model.OpenHours;
import com.example.changetheworld.model.PrivateClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditBusinessProfileActivity extends AppCompatActivity {

    String userName;
    EditText business_name;
    EditText mail_address;
    EditText phone_number;
    EditText owner_name;
    Spinner state;
    EditText city;
    EditText street;
    EditText number;
    EditText password;

    EditText sundayOpen, sundayClose, monThuOpen, monThuClose, fridayOpen, fridayClose, saturdayOpen, saturdayClose;

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
        password = findViewById(R.id.business_password_value);

        sundayOpen = findViewById(R.id.sundayOpen);
        sundayClose = findViewById(R.id.sundayClose);
        monThuOpen = findViewById(R.id.monThuOpen);
        monThuClose = findViewById(R.id.monThuClose);
        fridayOpen = findViewById(R.id.fridayOpen);
        fridayClose = findViewById(R.id.fridayClose);
        saturdayOpen = findViewById(R.id.saturdayOpen);
        saturdayClose = findViewById(R.id.saturdayClose);

        userName = getIntent().getStringExtra("user_name");
        ((TextView)findViewById(R.id.business_username_profile_name)).setText(userName);
        FireStoreDB.getInstance().loadBusinessDataForEdit(userName, business_name, mail_address, phone_number , owner_name, state, city, street, number, password, sundayOpen, sundayClose, monThuOpen, monThuClose, fridayOpen, fridayClose, saturdayOpen, saturdayClose);

        updateButton = findViewById(R.id.updateBtn);
        updateButton.setOnClickListener(view -> {

            String new_sundayOpen = sundayOpen.getText().toString();
            String new_sundayClose = sundayClose.getText().toString();
            String new_monThuOpen = monThuOpen.getText().toString();
            String new_monThuClose = monThuClose.getText().toString();
            String new_fridayOpen = fridayOpen.getText().toString();
            String new_fridayClose = fridayClose.getText().toString();
            String new_saturdayOpen = sundayOpen.getText().toString();
            String new_saturdayClose = sundayClose.getText().toString();

            ArrayList<OpenHours> openHours = new ArrayList<>();
            openHours.add(new OpenHours(new_sundayOpen, new_sundayClose));
            openHours.add(new OpenHours(new_monThuOpen, new_monThuClose));
            openHours.add(new OpenHours(new_fridayOpen, new_fridayClose));
            openHours.add(new OpenHours(new_saturdayOpen, new_saturdayClose));

            int invalidOpenHours = 0;

            for (OpenHours openHour: openHours) {
                try {
                    Date open = new SimpleDateFormat("HH:mm").parse(openHour.getOpen());
                    Date close = new SimpleDateFormat("HH:mm").parse(openHour.getClose());
                    if (open.compareTo(close) > 0) {
                        Toast.makeText(this, "Opening hour cannot be later than closing hour", Toast.LENGTH_SHORT).show();
                        invalidOpenHours = 1;
                    }
                } catch (ParseException e) {
                    Toast.makeText(this, "Invalid open hour", Toast.LENGTH_SHORT).show();
                    invalidOpenHours = 1;
                }
            }

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
            else if (new_state.isEmpty()){
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
            else if (invalidOpenHours == 1){}
            else {
                BusinessClient business_client = new BusinessClient(new_business_name,new_mail_address,new_phone_number,userName,new_password,new_owner_name,null, null,new_state,new_city, new_street, new_number);
                Intent intent = new Intent(this, BusinessProfileActivity.class);
                FireStoreDB.getInstance().updateBusinessProfile(this, business_client, openHours, intent);
            }
        });
    }
}