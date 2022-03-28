package com.example.changetheworld.model;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class CurrencyDataApi implements CurrencyDataApiInterface{



    private String api_prefix = "https://api.polygon.io/v2/aggs/ticker/C:";
    private String api_range = "/range/1/day/";
    private String api_key = "?apiKey=4CY1JucBSQeEAM5vpDZHXEBDRvEFwJsp";


    String start_date = LocalDate.now().minusDays(1).toString().replace("/","-");
    String end_date = LocalDate.now().minusDays(2).toString().replace("/","-");
    String api_suffix = api_range + start_date + "/" + end_date + api_key;
    DecimalFormat df = new DecimalFormat("0.000");




    private float getClosePrice(String base_currency, String currency_name, int day){
        String pair = base_currency + currency_name;

        String query = api_prefix + pair + api_suffix;
        try {
            URL url = new URL(query);;
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            Scanner s = new Scanner(new BufferedReader(new InputStreamReader(in)));
            JSONObject data = null;
            String result = s.next();
            data = new JSONObject(result);
            JSONArray results = data.getJSONArray("results");
            JSONObject last_day = (JSONObject) results.get(day);
            urlConnection.disconnect();
            return Float.parseFloat(df.format(Float.parseFloat(last_day.getString("c"))));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public float GetCurrencyValue(String base_currency, String currency_name) {
        return  getClosePrice(base_currency,currency_name,1);
    }

    @Override
    public float GetCurrencyDailyChange(String base_currency, String currency_name) {
        float last_close = GetCurrencyValue(base_currency,currency_name);
        float prev_close = getClosePrice(base_currency, currency_name, 0);
        if (last_close == 0.0F || prev_close == 0.0F){
            return 0.0F;
        }
        float daily_change = ((last_close - prev_close) / prev_close) * 100;
        return Float.parseFloat(df.format(daily_change));
    }
}
