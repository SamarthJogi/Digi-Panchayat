package com.digipanchayat.dto;

public class NewConnectionRequestDTO {
    private Long id;
    private String serviceType;
    private String applicantName;
    private String address;
    private String phone;
    private Long consumerId;
    private String aadharDocument;
    private String panDocument;
    private String status;
    private String appliedDate;
    private String resolutionDate;

    public NewConnectionRequestDTO() {
    }

    public NewConnectionRequestDTO(Long id, String serviceType, String applicantName, String address, String phone,
            Long consumerId,
            String aadharDocument, String panDocument, String status, String appliedDate, String resolutionDate) {
        this.id = id;
        this.serviceType = serviceType;
        this.applicantName = applicantName;
        this.address = address;
        this.phone = phone;
        this.consumerId = consumerId;
        this.aadharDocument = aadharDocument;
        this.panDocument = panDocument;
        this.status = status;
        this.appliedDate = appliedDate;
        this.resolutionDate = resolutionDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }

    public String getAadharDocument() {
        return aadharDocument;
    }

    public void setAadharDocument(String aadharDocument) {
        this.aadharDocument = aadharDocument;
    }

    public String getPanDocument() {
        return panDocument;
    }

    public void setPanDocument(String panDocument) {
        this.panDocument = panDocument;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(String appliedDate) {
        this.appliedDate = appliedDate;
    }

    public String getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(String resolutionDate) {
        this.resolutionDate = resolutionDate;
    }
}
