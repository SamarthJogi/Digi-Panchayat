package com.digipanchayat.dto;

public class GalleryDTO {
    private Long id;
    private String title;
    private String description;
    private String imageData;
    private String uploadDate;

    public GalleryDTO() {
    }

    public GalleryDTO(Long id, String title, String description, String imageData, String uploadDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageData = imageData;
        this.uploadDate = uploadDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }
}
