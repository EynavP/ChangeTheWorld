package com.example.changetheworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changetheworld.model.CurrencyDataApi;
import com.example.changetheworld.model.CurrencyDataApiInterface;
import com.example.changetheworld.model.FireStoreDB;
import com.google.type.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DepositPage extends AppCompatActivity {

    Button deposit;
    String userName;
    String userType;
    String subWalletName;
    String localCurrencySymbol;
    TextView toCurrency;
    TextView fromCurrency;
    EditText fromAmount;
    TextView toAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_page);

        subWalletName = getIntent().getStringExtra("subWalletName");
        userName = getIntent().getStringExtra(getString(R.string.userName));
        userType = getIntent().getStringExtra("userType");
        localCurrencySymbol = getIntent().getStringExtra("localCurrencySymbol");
        toAmount = findViewById(R.id.toAmonut);
        String local_currency = FireStoreDB.getInstance().symbolToCurrency.get(localCurrencySymbol);
        String wallet_currency = subWalletName;
        ArrayList<String> symbols = new ArrayList<>();
        symbols.add(local_currency + '/' + wallet_currency);
        CurrencyDataApiInterface api = new CurrencyDataApi();

        fromAmount = findViewById(R.id.fromAmonut);
        fromAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Thread t = new Thread(() -> {
                    String result;
                    Float value;
                    if (!editable.toString().isEmpty())
                        value = Float.parseFloat(editable.toString());
                    else
                        value = Float.parseFloat("0");

                    if(local_currency.equals(wallet_currency)){
                       result =String.valueOf(value);
                    }else{
                        HashMap<String, ArrayList<Float>> rate = api.getCloseAndChangePrice(symbols);
                        result = String.valueOf(rate.get(local_currency).get(0) * value);
                    }
                    runOnUiThread(() -> toAmount.setText(result));
                });
                t.start();
            }
        });

        toCurrency = findViewById(R.id.toCurrency);
        toCurrency.setText(subWalletName);

        fromCurrency = findViewById(R.id.FromCurrency);
        fromCurrency.setText(FireStoreDB.getInstance().symbolToCurrency.get(localCurrencySymbol));

        deposit = findViewById(R.id.depositBtn);
        deposit.setOnClickListener(view -> {
            String from = ((EditText) findViewById(R.id.fromAmonut)).getText().toString();
            String cardNumber = ((EditText) findViewById(R.id.cardNumber)).getText().toString();
            String ownerName = ((EditText) findViewById(R.id.ownerName)).getText().toString();
            String ownerId = ((EditText) findViewById(R.id.ownerId)).getText().toString();
            String expireDate = ((EditText) findViewById(R.id.expireDate)).getText().toString();
            String cvv = ((EditText) findViewById(R.id.CVV)).getText().toString();
            String to = toAmount.getText().toString();

            DateTimeFormatter ccMonthFormatter = DateTimeFormatter.ofPattern("MM/uu");

            try {
                YearMonth lastValidMonth = YearMonth.parse(expireDate, ccMonthFormatter);


            if (from.isEmpty() || Float.parseFloat(from) <= 0){
                Toast toast = Toast.makeText(this, R.string.from_invalid,Toast.LENGTH_LONG);
                toast.show();
            }
            else if (cardNumber.isEmpty()){
                Toast toast = Toast.makeText(this, R.string.carnNumber_invalid,Toast.LENGTH_LONG);
                toast.show();
            }
            else if (ownerName.isEmpty() || !ownerName.matches("[a-zA-z\\s]*$")){
                Toast toast = Toast.makeText(this, R.string.ownerName_invalid,Toast.LENGTH_LONG);
                toast.show();
            }
            else if (ownerId.isEmpty()){
                Toast toast = Toast.makeText(this, R.string.ownerId_invalid,Toast.LENGTH_LONG);
                toast.show();
            }
            else if (expireDate.isEmpty() || YearMonth.now(ZoneId.systemDefault()).isAfter(lastValidMonth)){
                Toast toast = Toast.makeText(this, R.string.expireDate_invalid,Toast.LENGTH_LONG);
                toast.show();
            }
            else if (cvv.isEmpty() || cvv.length() != 3){
                Toast toast = Toast.makeText(this, R.string.cvv_invalid,Toast.LENGTH_LONG);
                toast.show();
            }
            else if (to == null || to.isEmpty()){
                Toast toast = Toast.makeText(this, R.string.invalid_deposite_amount,Toast.LENGTH_LONG);
                toast.show();
            }
            else {
                Intent intent = new Intent(this, SubWallet.class);
                intent.putExtra("subWalletName",subWalletName);
                intent.putExtra(getString(R.string.userName), userName);
                intent.putExtra("userType", userType);
                intent.putExtra("localCurrencySymbol",localCurrencySymbol);
                FireStoreDB.getInstance().updateBalance(userName, userType, subWalletName, Float.parseFloat(to), "+", this, intent);
            }
            } catch (DateTimeParseException dtpe) {
                Toast toast = Toast.makeText(this, R.string.expireDate_invalid,Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

}