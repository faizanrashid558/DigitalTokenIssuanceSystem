package com.faizanrashid.digitaltokenissuancesystem.Customer;

public class Token {
    public String uid, name, email, phone, tokenNo, tokenType, payment, status;

    public Token(){

    }

    public Token(String uid, String name, String email, String phone, String tokenNo, String tokenType, String payment, String status) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.tokenNo = tokenNo;
        this.tokenType = tokenType;
        this.payment = payment;
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTokenNo() {
        return tokenNo;
    }

    public void setTokenNo(String tokenNo) {
        this.tokenNo = tokenNo;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
