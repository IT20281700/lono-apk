package com.chamodex.enterprises.loano.models;

public class Users {
    private String email, name, mobile, address, dob, gender, accountNo;
    public Users() {}

    public Users(String email, String name, String mobile, String address, String dob, String gender, String accountNo) {
        this.email = email;
        this.name = name;
        this.mobile = mobile;
        this.address = address;
        this.dob = dob;
        this.gender = gender;
        this.accountNo = accountNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
