package com.digipanchayat.dto;

public class PropertyTaxRequest {
    private String consumerServiceNumber;
    private Double area;
    private String usageType; // RESIDENTIAL, RENTED, COMMERCIAL

    // Constructors
    public PropertyTaxRequest() {
    }

    public PropertyTaxRequest(String consumerServiceNumber, Double area, String usageType) {
        this.consumerServiceNumber = consumerServiceNumber;
        this.area = area;
        this.usageType = usageType;
    }

    // Getters
    public String getConsumerServiceNumber() {
        return consumerServiceNumber;
    }

    public Double getArea() {
        return area;
    }

    public String getUsageType() {
        return usageType;
    }

    // Setters
    public void setConsumerServiceNumber(String consumerServiceNumber) {
        this.consumerServiceNumber = consumerServiceNumber;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }
}
