package com.digipanchayat.controller;

import com.digipanchayat.dto.ApiResponse;
import com.digipanchayat.dto.ComplaintRequest;
import com.digipanchayat.entity.Complaint;
import com.digipanchayat.service.ComplaintService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/complaints")

public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerComplaint(@RequestBody ComplaintRequest request) {
        try {
            Complaint complaint = complaintService.registerComplaint(request);
            return ResponseEntity.ok(new ApiResponse(true, "Complaint registered successfully", complaint));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/consumer/{consumerId}")
    public ResponseEntity<ApiResponse> getConsumerComplaints(@PathVariable Long consumerId) {
        try {
            List<Complaint> complaints = complaintService.getConsumerComplaints(consumerId);
            return ResponseEntity.ok(new ApiResponse(true, "Complaints retrieved successfully", complaints));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllComplaints() {
        try {
            List<Complaint> complaints = complaintService.getAllComplaints();
            return ResponseEntity.ok(new ApiResponse(true, "Complaints retrieved successfully", complaints));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/{complaintId}/status")
    public ResponseEntity<ApiResponse> updateComplaintStatus(@PathVariable Long complaintId,
            @RequestParam String status) {
        try {
            Complaint complaint = complaintService.updateComplaintStatus(complaintId, status);
            return ResponseEntity.ok(new ApiResponse(true, "Complaint status updated", complaint));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}
