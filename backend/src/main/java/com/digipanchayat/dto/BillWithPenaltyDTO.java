package com.digipanchayat.dto;

public class BillWithPenaltyDTO {
    private Long id;
    private String billType;
    private String consumerServiceNumber;
    private String billDate;
    private String dueDate;
    private Double baseAmount;
    private Double penaltyAmount;
    private Double totalAmount;
    private Integer monthsDelayed;
    private String status;
    private Double area;
    private String usageType;

    public BillWithPenaltyDTO() {
    }

    public BillWithPenaltyDTO(Long id, String billType, String consumerServiceNumber, String billDate,
            String dueDate, Double baseAmount, Double penaltyAmount, Double totalAmount,
            Integer monthsDelayed, String status, Double area, String usageType) {
        this.id = id;
        this.billType = billType;
        this.consumerServiceNumber = consumerServiceNumber;
        this.billDate = billDate;
        this.dueDate = dueDate;
        this.baseAmount = baseAmount;
        this.penaltyAmount = penaltyAmount;
        this.totalAmount = totalAmount;
        this.monthsDelayed = monthsDelayed;
        this.status = status;
        this.area = area;
        this.usageType = usageType;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getConsumerServiceNumber() {
        return consumerServiceNumber;
    }

    public void setConsumerServiceNumber(String consumerServiceNumber) {
        this.consumerServiceNumber = consumerServiceNumber;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Double getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(Double baseAmount) {
        this.baseAmount = baseAmount;
    }

    public Double getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(Double penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getMonthsDelayed() {
        return monthsDelayed;
    }

    public void setMonthsDelayed(Integer monthsDelayed) {
        this.monthsDelayed = monthsDelayed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }
}
