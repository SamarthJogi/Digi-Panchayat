package com.digipanchayat.service;

import com.digipanchayat.dto.GalleryDTO;
import com.digipanchayat.entity.Gallery;
import com.digipanchayat.repository.GalleryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GalleryService {

    @Autowired
    private GalleryRepository galleryRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<GalleryDTO> getAllImages() {
        List<Gallery> images = galleryRepository.findAll();
        return images.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public GalleryDTO addImage(GalleryDTO galleryDTO) {
        // Validate file size
        if (galleryDTO.getImageData() != null && galleryDTO.getImageData().length() > 700000) {
            throw new RuntimeException("Image size exceeds 500KB limit");
        }

        Gallery gallery = new Gallery();
        gallery.setTitle(galleryDTO.getTitle());
        gallery.setDescription(galleryDTO.getDescription());
        gallery.setImageData(galleryDTO.getImageData());
        gallery.setUploadDate(LocalDate.now().format(DATE_FORMATTER));

        Gallery saved = galleryRepository.save(gallery);
        return convertToDTO(saved);
    }

    public void deleteImage(Long id) {
        galleryRepository.deleteById(id);
    }

    private GalleryDTO convertToDTO(Gallery gallery) {
        return new GalleryDTO(
                gallery.getId(),
                gallery.getTitle(),
                gallery.getDescription(),
                gallery.getImageData(),
                gallery.getUploadDate());
    }
}
