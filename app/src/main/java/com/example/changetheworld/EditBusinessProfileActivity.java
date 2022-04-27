package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changetheworld.model.BusinessClient;
import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.OpenHours;
import com.example.changetheworld.model.PrivateClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    int SELECT_PICTURE = 200;
    byte[] Business_approval_document;
    byte[] Business_owner_id;
    int flag = 0;


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

        //Business approval document load
        ImageView Business_approval_documentIcon = findViewById(R.id.uploadapprovalicon);
        Business_approval_documentIcon.setOnClickListener(v -> {
                    flag = 1;
                    imageChooser();
                }
        );

        //owner_id load
        ImageView owner_idIcon = (findViewById(R.id.ApprovalDocumentIcon));
        owner_idIcon.setOnClickListener(v -> {
                    flag = 2;
                    imageChooser();
                }
        );

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
            else if (invalidOpenHours == 1){}
            else {
                BusinessClient business_client = new BusinessClient(new_business_name,new_mail_address,new_phone_number,userName,new_password,new_owner_name,Business_approval_document, Business_owner_id,new_state,new_city, new_street, new_number);
                Intent intent = new Intent(this, BusinessProfileActivity.class);
                FireStoreDB.getInstance().updateBusinessProfile(this, business_client, openHours, intent);
            }
        });
    }

    public void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType(getString(R.string.image_path));
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, getString(R.string.Select_Picture)), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] photo_byte = baos.toByteArray();

                        if (flag == 1){
                            Business_approval_document = photo_byte;
                            Toast.makeText(this, "Business approval document Upload Success", Toast.LENGTH_SHORT).show();
                        }

                        if (flag == 2) {
                            Business_owner_id = photo_byte;
                            Toast.makeText(this, "Business owner id Upload Success", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}