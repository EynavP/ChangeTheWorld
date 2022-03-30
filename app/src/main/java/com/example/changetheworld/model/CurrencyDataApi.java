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



    private String api_key = "&access_key=3p6PQYrYrFwyQ3FCaSag";
    private String base_api = "https://fcsapi.com/api-v3/forex/latest?";
    private String symbol = "symbol=";

    DecimalFormat df = new DecimalFormat("0.000");

    @Override
    public HashMap<String, ArrayList<Float>> getCloseAndChangePrice(ArrayList<String> pairs){
        String query = base_api + symbol;
        for (String pair: pairs) {
            query += pair + ',';
        }
        query = query.substring(0,  query.length() -1 );
        query += api_key;
        HashMap<String, ArrayList<Float>> currency_data = new HashMap<>();
        try {
            URL url = new URL(query);;
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            Scanner s = new Scanner(new BufferedReader(new InputStreamReader(in)));
            JSONObject data = null;
            String result = "";
            while (s.hasNext()){
                result += s.next();
            }
            data = new JSONObject(result);
            JSONArray results = data.getJSONArray("response");
            urlConnection.disconnect();
            for(int i = 0; i < results.length(); i++){
                JSONObject last_day = (JSONObject) results.get(i);
                Float close_price = Float.parseFloat(df.format(Float.parseFloat(last_day.getString("c"))));
                Float change_price = Float.parseFloat(df.format(Float.parseFloat(last_day.getString("cp").replace("%","").replace("+","").replace("-",""))));
                ArrayList<Float> price_data = new ArrayList<>();
                price_data.add(close_price);
                price_data.add(change_price);
                int index_of_end = last_day.getString("s").indexOf("/");
                String sym = last_day.getString("s").substring(0, index_of_end);
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


