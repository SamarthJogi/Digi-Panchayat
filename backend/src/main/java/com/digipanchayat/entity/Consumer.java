package com.digipanchayat.entity;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "consumer")
public class Consumer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "consumer_number", unique = true)
    private String consumerNumber;

    @Column(name = "status")
    private String status; // ACTIVE, INACTIVE

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "mobile", unique = true)
    private String mobile;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "role")
    private String role; // CONSUMER, ADMIN

    @Column(name = "otp")
    private String otp;

    @Column(name = "otp_expiry")
    private Long otpExpiry;

    @OneToMany(mappedBy = "consumer", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Bill> bills;

    @OneToMany(mappedBy = "consumer", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Complaint> complaints;

    // Default constructor
    public Consumer() {
    }

    // All-arguments constructor
    public Consumer(Long id, String consumerNumber, String status, String email, String mobile,
            String password, String name, String role, String otp, Long otpExpiry,
            List<Bill> bills, List<Complaint> complaints) {
        this.id = id;
        this.consumerNumber = consumerNumber;
        this.status = status;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.name = name;
        this.role = role;
        this.otp = otp;
        this.otpExpiry = otpExpiry;
        this.bills = bills;
        this.complaints = complaints;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getConsumerNumber() {
        return consumerNumber;
    }

    public String getStatus() {
        return status;
    }

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

    public String getRole() {
        return role;
    }

    public String getOtp() {
        return otp;
    }

    public Long getOtpExpiry() {
        return otpExpiry;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public List<Complaint> getComplaints() {
        return complaints;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setConsumerNumber(String consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public void setRole(String role) {
        this.role = role;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setOtpExpiry(Long otpExpiry) {
        this.otpExpiry = otpExpiry;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints;
    }
}
