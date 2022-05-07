package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changetheworld.model.FireStoreDB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OrderPage extends AppCompatActivity {

    Spinner from, to;
    EditText amount,pickup_date;
    Button cash, wallet, submit;
    String payment_method, business_user_name, client_user_name;
    TextView receive, pick_from;
    int mYear,mMonth,mDay;
    SimpleDateFormat sdf;
    String myFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);
        business_user_name = getIntent().getStringExtra(getString(R.string.business_user_name));
        client_user_name = getIntent().getStringExtra(getString(R.string.client_user_name));

        from = findViewById(R.id.from_value);
        to = findViewById(R.id.to_value);
        amount = findViewById(R.id.amount_value);
        cash = findViewById(R.id.cash_btn);
        wallet = findViewById(R.id.wallet_btn);
        receive = findViewById(R.id.receive_value);
        pick_from = findViewById(R.id.pickup_from_value);
        submit = findViewById(R.id.submit_btn);
        pickup_date =findViewById(R.id.pickup_date_value);

        FireStoreDB.getInstance().LoadBusinessAddress(this, business_user_name, pick_from);

        pickup_date.setOnClickListener(view -> {
            Calendar mcurrentDate = Calendar.getInstance();
            mYear = mcurrentDate.get(Calendar.YEAR);
            mMonth = mcurrentDate.get(Calendar.MONTH);
            mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(OrderPage.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    Calendar myCalendar = Calendar.getInstance();
                    myCalendar.set(Calendar.YEAR, selectedyear);
                    myCalendar.set(Calendar.MONTH, selectedmonth);
                    myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                    myFormat = "dd/MM/yy"; //Change as you need
                    sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                    pickup_date.setText(sdf.format(myCalendar.getTime()));

                    mDay = selectedday;
                    mMonth = selectedmonth;
                    mYear = selectedyear;
                }
            }, mYear, mMonth, mDay);
            //mDatePicker.setTitle("Select date");
            mDatePicker.show();
        });

        cash.setOnClickListener(view -> {
            payment_method = "cash";
            cash.setBackground(getResources().getDrawable(R.drawable.pressed_button));
            wallet.setBackground(getResources().getDrawable(R.drawable.payment_method_btn));
        });
        wallet.setOnClickListener(view -> {
            payment_method = "wallet";
            wallet.setBackground(getResources().getDrawable(R.drawable.pressed_button));
            cash.setBackground(getResources().getDrawable(R.drawable.payment_method_btn));
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        adapter.addAll(FireStoreDB.getInstance().currenciesToSymbol.keySet());
        from.setAdapter(adapter);
        to.setAdapter(adapter);

        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty() && !from.getSelectedItem().toString().equals(to.getSelectedItem().toString())){
                    FireStoreDB.getInstance().calculateChangeRate(OrderPage.this, business_user_name, from.getSelectedItem().toString(),
                            to.getSelectedItem().toString(), Float.parseFloat(editable.toString()), receive);
                }
                else if(editable.toString().isEmpty()){
                    receive.setText("");
                    Toast.makeText(OrderPage.this, getString(R.string.Amount_shouldnt_be_zero) , Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(OrderPage.this, getString(R.string.currencies_must_be_diffrent), Toast.LENGTH_SHORT).show();
            }
        });

        submit.setOnClickListener(view -> {
            String from_currency = from.getSelectedItem().toString();
            String to_currency = to.getSelectedItem().toString();
            String from_amount = amount.getText().toString();
            String to_amount = receive.getText().toString();
            String date = pickup_date.getText().toString();
            String business_address = pick_from.getText().toString();
            Date order_date = null;
            Date current_date = new Date();

            try {
                order_date =  sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if(from_amount.isEmpty())
                Toast.makeText(this, "Enter amount to convert", Toast.LENGTH_SHORT).show();
            else if(to_currency.equals(from_currency))
                Toast.makeText(this, "Pair must be different", Toast.LENGTH_SHORT).show();
            else if(date.isEmpty())
                Toast.makeText(this, "Please enter date",Toast.LENGTH_SHORT).show();
            else if(payment_method == null  || payment_method.isEmpty())
                Toast.makeText(this, "Please Choose payment method", Toast.LENGTH_SHORT).show();
            else if( order_date == null || order_date.compareTo(current_date) < 0){
                Toast.makeText(this, "Invalid Date",Toast.LENGTH_SHORT).show();
            }
            else if(payment_method.equals("cash")){
                FireStoreDB.getInstance().PayByCash(this, "PrivateClient", business_user_name, client_user_name, from_currency, to_currency, from_amount, to_amount, date , business_address);
            }
            else if(payment_method.equals("wallet")){
                FireStoreDB.getInstance().PayByWallet(this, "PrivateClient", business_user_name, client_user_name, from_currency, to_currency, from_amount, to_amount, date , business_address);
            }
        });







    }





}