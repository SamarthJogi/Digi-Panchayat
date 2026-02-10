package com.digipanchayat.service;

import com.digipanchayat.dto.NewConnectionRequestDTO;
import com.digipanchayat.entity.Consumer;
import com.digipanchayat.entity.NewConnectionRequest;
import com.digipanchayat.repository.ConsumerRepository;
import com.digipanchayat.repository.NewConnectionRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;

@Service
public class NewConnectionService {

    @Autowired
    private NewConnectionRequestRepository connectionRequestRepository;

    @Autowired
    private ConsumerRepository consumerRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public NewConnectionRequestDTO applyForConnection(NewConnectionRequestDTO requestDTO) {
        // Validate file sizes (base64 check - rough estimate: base64 is ~1.33x original
        // size)
        if (requestDTO.getAadharDocument() != null && requestDTO.getAadharDocument().length() > 700000) {
            throw new RuntimeException("Aadhar document size exceeds 500KB limit");
        }
        if (requestDTO.getPanDocument() != null && requestDTO.getPanDocument().length() > 700000) {
            throw new RuntimeException("PAN document size exceeds 500KB limit");
        }

        Consumer consumer = consumerRepository.findById(requestDTO.getConsumerId())
                .orElseThrow(() -> new RuntimeException("Consumer not found"));

        NewConnectionRequest request = new NewConnectionRequest();
        request.setServiceType(requestDTO.getServiceType());
        request.setServiceType(requestDTO.getServiceType());
        request.setApplicantName(requestDTO.getApplicantName());
        request.setAddress(requestDTO.getAddress());
        request.setPhone(requestDTO.getPhone());
        request.setConsumer(consumer);
        request.setAadharDocument(requestDTO.getAadharDocument());
        request.setPanDocument(requestDTO.getPanDocument());
        request.setStatus("PENDING");
        request.setAppliedDate(LocalDateTime.now().format(DATE_FORMATTER));

        NewConnectionRequest saved = connectionRequestRepository.save(request);
        return convertToDTO(saved);
    }

    public List<NewConnectionRequestDTO> getConsumerRequests(Long consumerId) {
        // We can't easily sort by ID in the repository method for specific consumer
        // without changing signature heavily,
        // but we can sort the list here.
        List<NewConnectionRequest> requests = connectionRequestRepository.findByConsumerId(consumerId);
        requests.sort((a, b) -> b.getId().compareTo(a.getId())); // LIFO sort
        return requests.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<NewConnectionRequestDTO> getAllRequests() {
        List<NewConnectionRequest> requests = connectionRequestRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return requests.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<NewConnectionRequestDTO> getPendingRequests() {
        List<NewConnectionRequest> requests = connectionRequestRepository.findByStatus("PENDING");
        requests.sort((a, b) -> b.getId().compareTo(a.getId())); // LIFO sort
        return requests.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public NewConnectionRequestDTO updateStatus(Long id, String status) {
        NewConnectionRequest request = connectionRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Connection request not found"));

        request.setStatus(status);
        if (!status.equals("PENDING")) {
            request.setResolutionDate(LocalDateTime.now().format(DATE_FORMATTER));
        }

        NewConnectionRequest updated = connectionRequestRepository.save(request);
        return convertToDTO(updated);
    }

    private NewConnectionRequestDTO convertToDTO(NewConnectionRequest request) {
        NewConnectionRequestDTO dto = new NewConnectionRequestDTO();
        dto.setId(request.getId());
        dto.setServiceType(request.getServiceType());
        dto.setServiceType(request.getServiceType());
        dto.setApplicantName(request.getApplicantName());
        dto.setAddress(request.getAddress());
        dto.setPhone(request.getPhone());
        dto.setConsumerId(request.getConsumer().getId());
        dto.setStatus(request.getStatus());
        dto.setAppliedDate(request.getAppliedDate());
        dto.setResolutionDate(request.getResolutionDate());
        // Don't send documents back in list responses for performance
        return dto;
    }

    public NewConnectionRequestDTO getRequestById(Long id) {
        NewConnectionRequest request = connectionRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Connection request not found"));

        NewConnectionRequestDTO dto = convertToDTO(request);
        // Include documents when specifically requested
        dto.setAadharDocument(request.getAadharDocument());
        dto.setPanDocument(request.getPanDocument());
        return dto;
    }
}
