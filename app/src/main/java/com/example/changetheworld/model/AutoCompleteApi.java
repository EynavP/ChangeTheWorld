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
import java.util.ArrayList;
import java.util.Scanner;

public class AutoCompleteApi implements AutoCompleteInterface{
    private String base_api = "http://193.106.55.105:80?autocomplete=";

    @Override
    public ArrayList<String> getComplete(String query) {
        query = base_api + query;
        URL url = null;
        try {
            url = new URL(query);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            Scanner s = new Scanner(new BufferedReader(new InputStreamReader(in)));
            String result = "";
            while (s.hasNext()){
                result += s.next() + " ";
            }
            if(result.charAt(result.length() -1) != ']'){
                result += ']';
            }
            urlConnection.disconnect();
            JSONArray data = new JSONArray(result);
            ArrayList<String> res = new ArrayList<>();
            for (int i = 0; i<data.length(); i++){
                res.add(data.getString(i));
            }
            return res;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
