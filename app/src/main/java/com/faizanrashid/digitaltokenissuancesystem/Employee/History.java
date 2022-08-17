package com.faizanrashid.digitaltokenissuancesystem.Employee;

public class History {
    String name, email, phone, tokenno, tokentype, payment, tokenstatus, cUid;
    public History(){}

    public History(String name, String email, String phone, String tokenno, String tokentype, String payment, String tokenstatus, String cUid) {

        this.name = name;
        this.email = email;
        this.phone = phone;
        this.tokenno = tokenno;
        this.tokentype = tokentype;
        this.payment = payment;
        this.tokenstatus = tokenstatus;
        this.cUid = cUid;
    }

    public String getcUid() {
        return cUid;
    }

    public void setcUid(String cUid) {
        this.cUid = cUid;
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

    public String getTokenno() {
        return tokenno;
    }

    public void setTokenno(String tokenno) {
        this.tokenno = tokenno;
    }

    public String getTokentype() {
        return tokentype;
    }

    public void setTokentype(String tokentype) {
        this.tokentype = tokentype;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getTokenstatus() {
        return tokenstatus;
    }

    public void setTokenstatus(String tokenstatus) {
        this.tokenstatus = tokenstatus;
    }
}
