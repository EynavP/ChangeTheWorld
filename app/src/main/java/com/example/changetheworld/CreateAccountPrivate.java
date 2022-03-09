package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.changetheworld.model.DataBaseInterface;
import com.example.changetheworld.model.FireStoreDB;
import com.example.changetheworld.model.PrivateClient;


public class CreateAccountPrivate extends AppCompatActivity {
    Button returnLogin;
    Spinner currencies;
    String[] currency = {"Choose Currency","DOLLAR","EURO"};
    int SELECT_PICTURE = 200;
    String personal_chosen_photo = "";
    String passport_chosen_photo = "";
    int flag = 0;


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




        //Personal photo load
        Button personal_photo = ((Button) findViewById(R.id.uploadPhoto));
        personal_photo.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    flag = 1;
                    imageChooser();
                }
            }
        );

        //Passport photo load
        Button passport_photo = ((Button) findViewById(R.id.uploadPassport));
        passport_photo.setOnClickListener(new View.OnClickListener()
           {
               @Override
               public void onClick(View v) {
                   flag = 2;
                   imageChooser();
               }
           }
        );

        Button CreateAccountPrivate = findViewById(R.id.createAccountButton);
        CreateAccountPrivate.setOnClickListener(view -> {
            String full_name = ((EditText) findViewById(R.id.enterFullName)).getText().toString();
            String mail = ((EditText) findViewById(R.id.enterMailAddress)).getText().toString();
            String phone = ((EditText) findViewById(R.id.enterPhoneNumber)).getText().toString();
            String currency = ((Spinner) findViewById(R.id.enterLocalCurrency)).getSelectedItem().toString();
            String user_name = ((EditText) findViewById(R.id.enterNewUserName)).getText().toString();
            String password = ((EditText) findViewById(R.id.enterNewPassword)).getText().toString();

            if (full_name.isEmpty() || !full_name.matches("[a-zA-z\\s]*$")){
                Toast toast = Toast.makeText(this, "Invalid full name", Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (mail.isEmpty() || !mail.matches("^(.+)@(\\S+)$")){
                Toast toast = Toast.makeText(this, "Invalid mail", Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (phone.isEmpty() || !phone.matches("^[0-9]*$")){
                Toast toast = Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (currency.isEmpty() || currency.equals("Choose Currency")){
                Toast toast = Toast.makeText(this, "Invalid currency", Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (user_name.isEmpty() || user_name.contains(" ")){
                Toast toast = Toast.makeText(this, "Invalid username", Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (password.isEmpty() || !password.matches("^[A-Za-z0-9]*$")){
                Toast toast = Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (personal_chosen_photo.isEmpty()){
                Toast toast = Toast.makeText(this, "Invalid personal picture", Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (passport_chosen_photo.isEmpty()){
                Toast toast = Toast.makeText(this, "Invalid passport picture", Toast.LENGTH_SHORT);
                toast.show();
            }else{
                //TODO: ADD DATABASE HERE
                PrivateClient client = new PrivateClient(user_name,full_name,mail,phone,currency,password,personal_chosen_photo,passport_chosen_photo);
                FireStoreDB.getInstance().VerifyAndSavePrivateClient(this, client);
            }
        });
    }

    public void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
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
                    if (flag == 1){
                        personal_chosen_photo = selectedImageUri.toString();
                        Toast.makeText(CreateAccountPrivate.this, "Photo Upload Success", Toast.LENGTH_SHORT).show();
                    }
                    }
                    if (flag == 2) {
                        passport_chosen_photo = selectedImageUri.toString();
                        Toast.makeText(CreateAccountPrivate.this, "Passport Upload Success", Toast.LENGTH_SHORT).show();
                    }
            }
        }
    }



    public void returnLoginPage(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


}