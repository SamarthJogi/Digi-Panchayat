package com.digipanchayat.dto;

public class RegisterRequest {
    private String email;
    private String mobile;
    private String password;
    private String name;
    private String address;

    // Constructors
    public RegisterRequest() {
    }

    public RegisterRequest(String email, String mobile, String password, String name) {
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.name = name;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
