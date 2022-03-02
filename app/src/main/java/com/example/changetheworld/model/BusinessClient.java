package com.example.changetheworld.model;

public class BusinessClient {
    private String business_name;
    private String mail_address;
    private String state;
    private String phone;
    private String user_name;
    private String password;
    private String business_owner_name;
    private String business_approval_document;
    private String getBusiness_owner_id;


    public BusinessClient(String business_name, String mail_address, String state, String phone, String user_name, String password, String business_owner_name, String business_approval_document, String getBusiness_owner_id) {
        this.business_name = business_name;
        this.mail_address = mail_address;
        this.state = state;
        this.phone = phone;
        this.user_name = user_name;
        this.password = password;
        this.business_owner_name = business_owner_name;
        this.business_approval_document = business_approval_document;
        this.getBusiness_owner_id = getBusiness_owner_id;
    }


    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getMail_address() {
        return mail_address;
    }

    public void setMail_address(String mail_address) {
        this.mail_address = mail_address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBusiness_owner_name() {
        return business_owner_name;
    }

    public void setBusiness_owner_name(String business_owner_name) {
        this.business_owner_name = business_owner_name;
    }

    public String getBusiness_approval_document() {
        return business_approval_document;
    }

    public void setBusiness_approval_document(String business_approval_document) {
        this.business_approval_document = business_approval_document;
    }

    public String getGetBusiness_owner_id() {
        return getBusiness_owner_id;
    }

    public void setGetBusiness_owner_id(String getBusiness_owner_id) {
        this.getBusiness_owner_id = getBusiness_owner_id;
    }
}
