package com.example.changetheworld.model;

import android.content.Context;

public interface DataBaseInterface {
    public void SavePrivateClient(Context context, PrivateClient user);
    public void SaveBusinessClient(Context context, BusinessClient user);
}
