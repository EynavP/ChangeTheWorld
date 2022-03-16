package com.example.changetheworld.model;

import android.widget.ImageView;

import java.util.Date;

public class Order
{
    int id;
    String fromCurrency;
    String toCurrency;
    float amount;
    float received;
    Date pickupDate;
    String clientName;
    String businessName;
    String status;
    ImageView QR_code;
    String paymentMethod;

    public Order(int id, String fromCurrency, String toCurrency, float amount, Date pickupDate, String clientName,
                 String businessName, String status, ImageView QR_code, String paymentMethod) {
        this.id = id;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;

        //TODO: get currency rate and calculate the receive value
        this.received = (float) (this.amount*0.3);

        this.pickupDate = pickupDate;
        this.clientName = clientName;
        this.businessName = businessName;
        this.status = status;
        this.QR_code = QR_code;
        this.paymentMethod = paymentMethod;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getFromCurrency() { return fromCurrency; }

    public void setFromCurrency(String fromCurrency) { this.fromCurrency = fromCurrency; }

    public String getToCurrency() { return toCurrency; }

    public void setToCurrency(String toCurrency) { this.toCurrency = toCurrency; }

    public float getAmount() { return amount; }

    public void setAmount(float amount) { this.amount = amount; }

    public float getReceived() { return received; }

    public void setReceived(float received) { this.received = received; }

    public Date getPickupDate() { return pickupDate; }

    public void setPickupDate(Date pickupDate) { this.pickupDate = pickupDate; }

    public String getClientName() { return clientName; }

    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getBusinessName() { return businessName; }

    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public ImageView getQR_code() { return QR_code; }

    public void setQR_code(ImageView QR_code) { this.QR_code = QR_code; }

    public String getPaymentMethod() { return paymentMethod; }

    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
