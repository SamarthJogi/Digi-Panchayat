package com.digipanchayat.controller;

import com.digipanchayat.dto.ApiResponse;
import com.digipanchayat.dto.GalleryDTO;
import com.digipanchayat.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gallery")

public class GalleryController {

    @Autowired
    private GalleryService galleryService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllImages() {
        try {
            List<GalleryDTO> images = galleryService.getAllImages();
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Gallery images retrieved successfully",
                    images));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addImage(@RequestBody GalleryDTO galleryDTO) {
        try {
            GalleryDTO result = galleryService.addImage(galleryDTO);
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Image added to gallery successfully",
                    result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long id) {
        try {
            galleryService.deleteImage(id);
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Image deleted successfully",
                    null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null));
        }
    }
}
