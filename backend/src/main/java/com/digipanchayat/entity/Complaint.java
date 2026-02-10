package com.digipanchayat.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "complaint")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "status")
    private String status; // PENDING, IN_PROGRESS, RESOLVED

    @Column(name = "type")
    private String type; // WATER, PROPERTY_TAX

    @Column(name = "complaint_date")
    private String complaintDate;

    @Column(name = "resolution_date")
    private String resolutionDate;

    @Column(name = "image_data", columnDefinition = "LONGTEXT")
    private String imageData; // Base64 encoded

    @ManyToOne
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;

    // Default constructor
    public Complaint() {
    }

    // All-arguments constructor
    public Complaint(Long id, String description, String status, String type,
            String complaintDate, String resolutionDate, String imageData, Consumer consumer) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.type = type;
        this.complaintDate = complaintDate;
        this.resolutionDate = resolutionDate;
        this.imageData = imageData;
        this.consumer = consumer;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getComplaintDate() {
        return complaintDate;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public String getResolutionDate() {
        return resolutionDate;
    }

    public String getImageData() {
        return imageData;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setComplaintDate(String complaintDate) {
        this.complaintDate = complaintDate;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public void setResolutionDate(String resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
}
