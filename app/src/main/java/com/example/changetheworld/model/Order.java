package com.example.changetheworld.model;

import android.widget.ImageView;

import java.util.Date;

public class Order
{
    String fromCurrency;
    String toCurrency;
    String amount;
    String received;
    String pickupDate;
    String name;
    String status;
    ImageView QR_code;
    String paymentMethod;

    public Order(String fromCurrency, String toCurrency, String amount, String received, String pickupDate, String name, String status, String paymentMethod) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
        this.received = received;
        this.pickupDate = pickupDate;
        this.name = name;
        this.status = status;
        this.QR_code = null;
        this.paymentMethod = paymentMethod;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ImageView getQR_code() {
        return QR_code;
    }

    public void setQR_code(ImageView QR_code) {
        this.QR_code = QR_code;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
