package com.example.changetheworld.model;

public interface CurrencyDataApiInterface {
    float GetCurrencyValue(String base_currency ,String currency_name);
    float GetCurrencyDailyChange(String base_currency, String currency_name);
}
