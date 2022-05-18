package com.example.changetheworld.model;

import java.util.HashMap;

public interface LocationDataApiInterface {
    Float GetDistance(String address_1, String address_2);
    HashMap<String, String> GetGeoLocation(String address);

}
