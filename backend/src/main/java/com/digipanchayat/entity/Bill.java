package com.digipanchayat.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "bill")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private String amount;

    @Column(name = "bill_date")
    private String billDate;

    @Column(name = "status")
    private String status; // PENDING, PAID

    @Column(name = "bill_type")
    private String billType; // WATER, PROPERTY_TAX

    @Column(name = "consumer_service_number")
    private String consumerServiceNumber; // WBP25XXXXXX or PTP25XXXXXX

    // For property tax
    @Column(name = "area")
    private Double area;

    @Column(name = "usage_type")
    private String usageType; // RESIDENTIAL, RENTED, COMMERCIAL

    @Column(name = "due_date")
    private String dueDate;

    @Column(name = "penalty_amount")
    private Double penaltyAmount;

    @Column(name = "months_delayed")
    private Integer monthsDelayed;

    @ManyToOne
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private List<Payment> payments;

    // Default constructor
    public Bill() {
    }

    // All-arguments constructor
    public Bill(Long id, String amount, String billDate, String dueDate, String status, String billType,
            String consumerServiceNumber, Double area, String usageType, Double penaltyAmount,
            Integer monthsDelayed, Consumer consumer, List<Payment> payments) {
        this.id = id;
        this.amount = amount;
        this.billDate = billDate;
        this.dueDate = dueDate;
        this.status = status;
        this.billType = billType;
        this.consumerServiceNumber = consumerServiceNumber;
        this.area = area;
        this.usageType = usageType;
        this.penaltyAmount = penaltyAmount;
        this.monthsDelayed = monthsDelayed;
        this.consumer = consumer;
        this.payments = payments;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public String getBillDate() {
        return billDate;
    }

    public String getStatus() {
        return status;
    }

    public String getBillType() {
        return billType;
    }

    public String getConsumerServiceNumber() {
        return consumerServiceNumber;
    }

    public Double getArea() {
        return area;
    }

    public String getUsageType() {
        return usageType;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public String getDueDate() {
        return dueDate;
    }

    public Double getPenaltyAmount() {
        return penaltyAmount;
    }

    public Integer getMonthsDelayed() {
        return monthsDelayed;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public void setConsumerServiceNumber(String consumerServiceNumber) {
        this.consumerServiceNumber = consumerServiceNumber;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setPenaltyAmount(Double penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public void setMonthsDelayed(Integer monthsDelayed) {
        this.monthsDelayed = monthsDelayed;
    }
}
