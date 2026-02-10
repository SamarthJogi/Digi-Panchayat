package com.digipanchayat.service;

import com.digipanchayat.dto.ComplaintRequest;
import com.digipanchayat.entity.Complaint;
import com.digipanchayat.entity.Consumer;
import com.digipanchayat.repository.ComplaintRepository;
import com.digipanchayat.repository.ConsumerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.data.domain.Sort;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final ConsumerRepository consumerRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final int RESOLUTION_DAYS = 7;

    public ComplaintService(ComplaintRepository complaintRepository, ConsumerRepository consumerRepository) {
        this.complaintRepository = complaintRepository;
        this.consumerRepository = consumerRepository;
    }

    public Complaint registerComplaint(ComplaintRequest request) {
        // Validate image size if provided
        if (request.getImageData() != null && request.getImageData().length() > 700000) {
            throw new RuntimeException("Image size exceeds 500KB limit");
        }

        Complaint complaint = new Complaint();
        complaint.setDescription(request.getDescription());
        complaint.setType(request.getType());
        complaint.setStatus("PENDING");

        LocalDateTime complaintDate = LocalDateTime.now();
        complaint.setComplaintDate(complaintDate.format(DATE_FORMATTER));

        // Set resolution date (7 days from complaint date)
        LocalDateTime resolutionDate = complaintDate.plusDays(RESOLUTION_DAYS);
        complaint.setResolutionDate(resolutionDate.format(DATE_FORMATTER));

        complaint.setImageData(request.getImageData());

        // Only set consumer if consumerId is provided
        if (request.getConsumerId() != null) {
            Consumer consumer = consumerRepository.findById(request.getConsumerId())
                    .orElseThrow(() -> new RuntimeException("Consumer not found"));
            complaint.setConsumer(consumer);
        }

        return complaintRepository.save(complaint);
    }

    public List<Complaint> getConsumerComplaints(Long consumerId) {
        List<Complaint> complaints = complaintRepository.findByConsumerId(consumerId);
        complaints.sort((a, b) -> b.getId().compareTo(a.getId())); // LIFO sort
        return complaints;
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Complaint updateComplaintStatus(Long complaintId, String status) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        complaint.setStatus(status);
        return complaintRepository.save(complaint);
    }

    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
    }
}
