package com.example.changetheworld.model;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

public interface DataBaseInterface {
    void VerifyAndSavePrivateClient(Context context, PrivateClient user);
    void VerifyAndSaveBusiness(Context context, BusinessClient businessClient);
    void VerifyAndPrivateClientLogin(Context context, String user_name, String password, Intent intent);
    void VerifyAndBusinessLogin(Context context, String user_name, String password, Intent intent);
    void LoadProfilePhoto(ImageView profilPhoto, String user_name);
}
