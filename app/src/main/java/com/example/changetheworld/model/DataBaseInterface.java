package com.example.changetheworld.model;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
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
    void searchChange(String state, String city, String street, String number, RecyclerView recyclerView, Context context);
}
