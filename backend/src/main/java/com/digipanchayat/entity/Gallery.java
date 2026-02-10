package com.digipanchayat.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "gallery")
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "image_data", columnDefinition = "LONGTEXT")
    private String imageData; // Base64 encoded

    @Column(name = "upload_date")
    private String uploadDate;

    // Default constructor
    public Gallery() {
    }

    // All-arguments constructor
    public Gallery(Long id, String title, String description, String imageData, String uploadDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageData = imageData;
        this.uploadDate = uploadDate;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageData() {
        return imageData;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }
}
