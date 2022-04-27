package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditClientProfileActivity extends AppCompatActivity {

    String userName;
    EditText full_name;
    EditText mail_address;
    EditText phone_number;
    EditText password;
    Spinner local_currency;
    ImageView profile;
    TextView passportValue;

    int SELECT_PICTURE = 200;
    byte[] personal_chosen_photo;
    byte[] passport_chosen_photo;
    int flag = 0;

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
        passportValue = findViewById(R.id.passport_value);
        FireStoreDB.getInstance().checkPassportPhoto(passportValue, userName);

        //Personal photo load
        ImageView personal_photo = findViewById(R.id.update_profile_photo);
        personal_photo.setOnClickListener(v -> {
                    flag = 1;
                    imageChooser();
                }
        );

        //Passport photo load
        ImageView passport_photo = (findViewById(R.id.uploadPassport));
        passport_photo.setOnClickListener(v -> {
                    flag = 2;
                    imageChooser();
                }
        );

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
            else if (new_password.isEmpty() || !new_password.matches("^[A-Za-z0-9]*$")) {
                Toast toast = Toast.makeText(this, getString(R.string.password_invalid), Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                PrivateClient client = new PrivateClient(userName,new_full_name,new_mail_address,new_phone_number, new_local_currency, new_password,personal_chosen_photo,passport_chosen_photo);
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
        FireStoreDB.getInstance().checkPassportPhoto(passportValue, userName);

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
                            personal_chosen_photo = photo_byte;
                            Toast.makeText(this, getString(R.string.photo_upload_success), Toast.LENGTH_SHORT).show();
                        }

                        if (flag == 2) {
                            passport_chosen_photo = photo_byte;
                            Toast.makeText(this, getString(R.string.passport_upload_success), Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}