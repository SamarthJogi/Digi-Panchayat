package com.digipanchayat.dto;

public class OtpVerifyRequest {
    private String emailOrMobile;
    private String otp;

    // Constructors
    public OtpVerifyRequest() {
    }

    public OtpVerifyRequest(String emailOrMobile, String otp) {
        this.emailOrMobile = emailOrMobile;
        this.otp = otp;
    }

    // Getters
    public String getEmailOrMobile() {
        return emailOrMobile;
    }

    public String getOtp() {
        return otp;
    }

    // Setters
    public void setEmailOrMobile(String emailOrMobile) {
        this.emailOrMobile = emailOrMobile;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
