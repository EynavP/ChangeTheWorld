package com.example.changetheworld.model;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class LocationDataApi implements LocationDataApiInterface{

    private String base_api = "http://193.106.55.105:80?location=";
    private String base_geo = "http://193.106.55.105:80?geolocation=";

    @Override
    public Float GetDistance(String address_1, String address_2) {
        String query = base_api + address_1 + '/' + address_2;
        URL url = null;
        try {
            url = new URL(query);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            Scanner s = new Scanner(new BufferedReader(new InputStreamReader(in)));
            String result = "";
            while (s.hasNext()){
                result += s.next();
            }
            if(result.charAt(result.length() -1) != ']'){
                result += ']';
            }
            urlConnection.disconnect();
            JSONArray data = new JSONArray(result);
            return Float.parseFloat(String.valueOf(data.get(0)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HashMap<String, String> GetGeoLocation(String address) {
        String query = base_geo + address;
        URL url = null;
        try {
            url = new URL(query);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            Scanner s = new Scanner(new BufferedReader(new InputStreamReader(in)));
            String result = "";
            while (s.hasNext()){
                result += s.next();
            }
            if(result.charAt(result.length() -1) != ']'){
                result += ']';
            }
            urlConnection.disconnect();
            JSONArray data = new JSONArray(result);



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
