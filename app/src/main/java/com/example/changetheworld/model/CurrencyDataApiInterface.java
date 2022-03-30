package com.example.changetheworld.model;

import java.util.ArrayList;
import java.util.HashMap;

public interface CurrencyDataApiInterface {
    HashMap<String, ArrayList<Float>> getCloseAndChangePrice(ArrayList<String> pairs);
}
