package com.example.changetheworld.model;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public interface DataBaseInterface {
    void VerifyAndSavePrivateClient(Context context, PrivateClient user, Intent intent);
    void VerifyAndSaveBusiness(Context context, BusinessClient businessClient, Intent intent);
    void VerifyAndLogin(Context context, String user_name, String password, Intent intent, String type);
    void LoadProfilePhoto(ImageView profilPhoto, String user_name);
    void LoadWallets(Context context, String user_name, String user_type, ArrayList<Wallet> items, RecyclerView recyclerView);
}
