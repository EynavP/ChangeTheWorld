package com.example.changetheworld.model;

import android.content.Context;
import android.content.Intent;

public interface DataBaseInterface {
    public void VerifyAndSavePrivateClient(Context context, PrivateClient user);
    public void VerifyAndSaveBusiness(Context context, BusinessClient businessClient);
    public void VerifyAndPrivateClientLogin(Context context, String user_name, String password, Intent intent);
    public void VerifyAndBusinessLogin(Context context, String user_name, String password);
}
