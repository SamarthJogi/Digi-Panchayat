package com.digipanchayat.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "new_connection_request")
public class NewConnectionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_type")
    private String serviceType; // WATER, PROPERTY

    @Column(name = "applicant_name")
    private String applicantName;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "status")
    private String status; // PENDING, APPROVED, REJECTED

    @ManyToOne
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;

    @Column(name = "applied_date")
    private String appliedDate;

    @Column(name = "resolution_date")
    private String resolutionDate;

    @Column(name = "aadhar_document", columnDefinition = "LONGTEXT")
    private String aadharDocument; // Base64 encoded

    @Column(name = "pan_document", columnDefinition = "LONGTEXT")
    private String panDocument; // Base64 encoded

    // Default constructor
    public NewConnectionRequest() {
    }

    // All-arguments constructor
    public NewConnectionRequest(Long id, String serviceType, String applicantName, String address, String phone,
            String status,
            Consumer consumer, String appliedDate, String resolutionDate,
            String aadharDocument, String panDocument) {
        this.id = id;
        this.serviceType = serviceType;
        this.applicantName = applicantName;
        this.address = address;
        this.phone = phone;
        this.status = status;
        this.consumer = consumer;
        this.appliedDate = appliedDate;
        this.resolutionDate = resolutionDate;
        this.aadharDocument = aadharDocument;
        this.panDocument = panDocument;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public String getAppliedDate() {
        return appliedDate;
    }

    public String getResolutionDate() {
        return resolutionDate;
    }

    public String getAadharDocument() {
        return aadharDocument;
    }

    public String getPanDocument() {
        return panDocument;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public void setAppliedDate(String appliedDate) {
        this.appliedDate = appliedDate;
    }

    public void setResolutionDate(String resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    public void setAadharDocument(String aadharDocument) {
        this.aadharDocument = aadharDocument;
    }

    public void setPanDocument(String panDocument) {
        this.panDocument = panDocument;
    }
}
