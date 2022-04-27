package com.example.changetheworld.model;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public interface DataBaseInterface {
    void VerifyAndSavePrivateClient(Context context, PrivateClient user, Intent intent);
    void VerifyAndSaveBusiness(Context context, BusinessClient businessClient, Intent intent);
    void VerifyAndLogin(Context context, String user_name, String password, Intent intent, String type);
    void LoadProfilePhoto(ImageView profilPhoto, String user_name);
    void LoadWallets(Context context, String user_name, String user_type, ArrayList<Wallet> items, RecyclerView recyclerView, TextView totalBalance, TextView symbol);
    void updateBalance(String user_name, String user_type, String wallet_name, float amount_in_foreign_currency, String action, Context context, Intent intent);
    void loadWalletHistory(String user_name, String user_type, String wallet_name, RecyclerView recyclerView, Context context);
    void searchChange(String searchQuery, String radius, RecyclerView recyclerView, Context context, ProgressBar progressBar);
    void loadClientData(String user_name, TextView full_name, TextView mail_address, TextView phone_number, TextView local_currency);
    void loadClientDataForEdit(String user_name, EditText full_name, EditText mail_address, EditText phone_number, Spinner local_currency, EditText password);
    void updateClientProfile(Context context, PrivateClient client, Intent intent);
    void loadBusinessData(String user_name, TextView business_name, TextView mail_address, TextView phone_number, TextView owner_name, TextView state, TextView city, TextView street, TextView number, TextView sundayHours, TextView monThuHours, TextView fridayHours, TextView saturdayHours);
    void loadBusinessDataForEdit(String user_name, EditText business_name,EditText mail_address, EditText phone_number, EditText owner_name, Spinner state, EditText city, EditText street, EditText number, EditText password, EditText sundayOpen, EditText sundayClose, EditText monThuOpen, EditText monThuClose,EditText fridayOpen, EditText fridayClose, EditText saturdayOpen, EditText saturdayClose);
    void updateBusinessProfile(Context context, BusinessClient business,ArrayList<OpenHours> openHours, Intent intent);
}
