package com.digipanchayat.dto;

public class OTPRequest {
    private String email;
    private String purpose; // "registration" or "login"

    public OTPRequest() {
    }

    public OTPRequest(String email, String purpose) {
        this.email = email;
        this.purpose = purpose;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
