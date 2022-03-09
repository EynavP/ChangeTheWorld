package com.example.changetheworld.model;

import android.content.Context;

public interface DataBaseInterface {
    public void VerifyAndSavePrivateClient(Context context, PrivateClient user);
    public void VerifyAndSaveBusiness(Context context, BusinessClient businessClient);
    public void VerifyAndPrivateClientLogin(Context context, String user_name, String password);
    public void VerifyAndBusinessLogin(Context context, String user_name, String password);
}
