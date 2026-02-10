package com.digipanchayat.dto;

public class LoginRequest {
    private String emailOrMobile;
    private String password;

    // Constructors
    public LoginRequest() {
    }

    public LoginRequest(String emailOrMobile, String password) {
        this.emailOrMobile = emailOrMobile;
        this.password = password;
    }

    // Getters
    public String getEmailOrMobile() {
        return emailOrMobile;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setEmailOrMobile(String emailOrMobile) {
        this.emailOrMobile = emailOrMobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
