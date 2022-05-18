package com.example.changetheworld.model;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public interface DataBaseInterface {
    void VerifyAndSavePrivateClient(Context context, PrivateClient user, Intent intent);

    void VerifyAndSaveBusiness(Context context, BusinessClient businessClient, Intent intent);

    void VerifyAndLogin(Context context, String user_name, String password, Intent intent, String type);

    void LoadProfilePhoto(ImageView profilPhoto, String user_name);

    void LoadWallets(Context context, String user_name, String user_type, ArrayList<Wallet> items, RecyclerView recyclerView, TextView totalBalance, TextView symbol);

    void updateBalance(String user_name, String user_type, String wallet_name, float amount_in_foreign_currency, String action, Context context, Intent intent);

    void loadWalletHistory(String user_name, String user_type, String wallet_name, RecyclerView recyclerView, Context context);

    void searchChange(String searchQuery, String radius, RecyclerView recyclerView, Context context, ProgressBar progressBar, ArrayList<Search> filter_list);

    void loadClientData(String user_name, TextView full_name, TextView mail_address, TextView phone_number, TextView local_currency);

    void loadClientDataForEdit(String user_name, EditText full_name, EditText mail_address, EditText phone_number, Spinner local_currency, EditText password);

    void updateClientProfile(Context context, PrivateClient client, Intent intent);

    void loadBusinessData(String user_name, TextView header, TextView business_name, TextView mail_address, TextView phone_number, TextView owner_name, TextView address, TextView local_currency, TextView sundayHours, TextView monThuHours, TextView fridayHours, TextView saturdayHours);

    void loadBusinessDataForEdit(String user_name, EditText business_name, EditText mail_address, EditText phone_number, EditText owner_name, Spinner local_currency, EditText address, EditText password, EditText sundayOpen, EditText sundayClose, EditText monThuOpen, EditText monThuClose, EditText fridayOpen, EditText fridayClose, EditText saturdayOpen, EditText saturdayClose );

    void updateBusinessProfile(Context context, BusinessClient business,ArrayList<OpenHours> openHours, Intent intent);

    void LoadOrdersStatus(Context context, TextView orders_for_today, TextView new_orders, TextView cash_orders, String user_type, String user_name);

    void checkExistPhoto(TextView value, String user_name, String type);

    void loadCurrencyRates(Context context, String user_name, RecyclerView recyclerView, ProgressBar progressBar, ArrayList<business_currency_rate> bcrs);

    void loadCurrencyRatesForShow(Context context, String user_name, RecyclerView recyclerView, ProgressBar progressBar, ArrayList<BusinessCurrencyRateForShow> bcrs);

    void saveChangeComissionRate(Context context, HashMap<String, String> comission_data, String business_user_name);

    void calculateChangeRate(Context context, String business_user_name, String from_currency, String to_currency, float amount, TextView receive);

    void LoadBusinessAddress(Context context, String business_user_name, TextView pick_from);

    void PayByCash(Context context, String user_type, String business_user_name, String client_user_name, String from_currency, String to_currency, String from_amount, String to_amount, String date, String business_address);

    void PayByWallet(Context context, String user_type, String business_user_name, String client_user_name, String from_currency, String to_currency, String from_amount, String to_amount, String date, String business_address);

    void LoadOrder(Context context, String orderID, String business_user_name, TextView amount_from, TextView amount_to, TextView paymethod, TextView business_name, TextView business_address, TextView business_phone, TextView pickup_date, TextView cash_case_value, TextView currency_from, TextView currency_to);

    void loadCurrencyDataPairs(Context context, String user_name, ArrayList<currency> items, RecyclerView recyclerView, ProgressBar progressBar, String user_type);

    void loadOrdersAsClient(Context context, String user_name, String userType, ArrayList<Order> items, RecyclerView recyclerView);

    void loadOrdersAsBusiness(Context context, String user_name, String user_type, ArrayList<Order> pendding_items, ArrayList<Order> canceled_items, ArrayList<Order> approve_items, ArrayList<Order> complete_items, RecyclerView PanddingrecyclerView, RecyclerView CanclerecyclerView, RecyclerView ApproverecyclerView, RecyclerView CompleterecyclerView);

    void openOnMaps(Context context, String address);

    void loadBusinessOrder(String orderID, String user_name, TextView order_status_value, TextView amount_value,
                           TextView currency_name_value, TextView receive_value, TextView to_currency_name_value, TextView payment_method_value, TextView client_name_value,
                           TextView phone_value, TextView pickup_date_value);

    void changeOrderStatus(String orderID, String user_name, String new_status, Context context, TextView order_status_value, Button approve_btn,Button cancel_btn);

    void updateBusinessRate(int rating, String business_user_name);

    void loadOrderRates(String user_name);
}
