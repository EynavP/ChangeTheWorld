package com.example.changetheworld.model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class CurrencyDataApi implements CurrencyDataApiInterface{

    private String base_api = "http://193.106.55.105:80?symbol=";
    DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public HashMap<String, ArrayList<Float>> getCloseAndChangePrice(ArrayList<String> pairs){
        String query = base_api;
        for (String pair: pairs) {
            query += pair + ',';
        }
        query = query.substring(0,  query.length() -1 );
        HashMap<String, ArrayList<Float>> currency_data = new HashMap<>();
        try {
            URL url = new URL(query);;
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            Scanner s = new Scanner(new BufferedReader(new InputStreamReader(in)));
            String result = "";
            while (s.hasNext()){
                result += s.next();
            }
            urlConnection.disconnect();
            JSONArray data = new JSONArray(result);
            for(int i = 0; i < data.length(); i++){
                JSONObject data_field = (JSONObject) data.get(i);
                Float close_price = Float.parseFloat(df.format(Float.parseFloat(data_field.getString("c"))));
                String change = data_field.getString("cp");
                Float change_price = Float.parseFloat(df.format(Float.parseFloat(change.replace("%","").replace("+","").replace("-",""))));
                if(change.contains("-")){
                    change_price *= -1;
                }
                ArrayList<Float> price_data = new ArrayList<>();
                price_data.add(close_price);
                price_data.add(change_price);
                int index_of_end = data_field.getString("s").indexOf("/");
                String sym = data_field.getString("s").substring(0,index_of_end);
                currency_data.put(sym, price_data);
            }
            return currency_data;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return currency_data;
    }
}


