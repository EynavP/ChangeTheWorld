package com.example.changetheworld.model;

import com.google.firebase.firestore.DocumentSnapshot;

import org.w3c.dom.Document;

public class PrivateClient {
    private String user_name;
    private String full_name;
    private String mail_address;
    private String phone_number;
    private String local_currency;
    private String password;
    private String photo;
    private String passport;
    final String KEY_FULL_NAME = "full_name";
    final String KEY_PHONE = "phone";
    final String KEY_MAIL = "mail";
    final String KEY_CURRENCY = "currency";
    final String KEY_USER_NAME = "user_name";
    final String KEY_PASSWORD = "password";
    final String KEY_PERSONAL_PHOTO = "personal_photo";
    final String KEY_PASSPORT_PHOTO = "passport_photo";



    public PrivateClient(String user_name, String full_name, String mail_address, String phone_number, String local_currency, String password, String photo, String passport) {
        this.user_name = user_name;
        this.full_name = full_name;
        this.mail_address = mail_address;
        this.phone_number = phone_number;
        this.local_currency = local_currency;
        this.password = password;
        this.photo = photo;
        this.passport = passport;
    }

    public PrivateClient(DocumentSnapshot documentSnapshot){
        this.user_name = documentSnapshot.getString(KEY_USER_NAME);
        this.full_name = documentSnapshot.getString(KEY_FULL_NAME);
        this.mail_address = documentSnapshot.getString(KEY_MAIL);
        this.phone_number = documentSnapshot.getString(KEY_PHONE);
        this.local_currency = documentSnapshot.getString(KEY_CURRENCY);
        this.password = documentSnapshot.getString(KEY_PASSWORD);
        this.photo = documentSnapshot.getString(KEY_PERSONAL_PHOTO);
        this.passport = documentSnapshot.getString(KEY_PASSPORT_PHOTO);
    }


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getMail_address() {
        return mail_address;
    }

    public void setMail_address(String mail_address) {
        this.mail_address = mail_address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getLocal_currency() {
        return local_currency;
    }

    public void setLocal_currency(String local_currency) {
        this.local_currency = local_currency;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }


}
