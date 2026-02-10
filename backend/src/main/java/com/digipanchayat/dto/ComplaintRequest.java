package com.digipanchayat.dto;

public class ComplaintRequest {
    private Long consumerId;
    private String description;
    private String type; // WATER, PROPERTY_TAX
    private String imageData; // Base64 encoded image

    // Constructors
    public ComplaintRequest() {
    }

    public ComplaintRequest(Long consumerId, String description, String type, String imageData) {
        this.consumerId = consumerId;
        this.description = description;
        this.type = type;
        this.imageData = imageData;
    }

    // Getters
    public Long getConsumerId() {
        return consumerId;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getImageData() {
        return imageData;
    }

    // Setters
    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
}
