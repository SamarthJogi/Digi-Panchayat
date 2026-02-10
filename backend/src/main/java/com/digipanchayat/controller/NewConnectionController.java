package com.digipanchayat.controller;

import com.digipanchayat.dto.ApiResponse;
import com.digipanchayat.dto.NewConnectionRequestDTO;
import com.digipanchayat.service.NewConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/connections")

public class NewConnectionController {

    @Autowired
    private NewConnectionService connectionService;

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse> applyForConnection(@RequestBody NewConnectionRequestDTO requestDTO) {
        try {
            NewConnectionRequestDTO result = connectionService.applyForConnection(requestDTO);
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Connection request submitted successfully",
                    result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null));
        }
    }

    @GetMapping("/consumer/{consumerId}")
    public ResponseEntity<ApiResponse> getConsumerRequests(@PathVariable Long consumerId) {
        try {
            List<NewConnectionRequestDTO> requests = connectionService.getConsumerRequests(consumerId);
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Requests retrieved successfully",
                    requests));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllRequests() {
        try {
            List<NewConnectionRequestDTO> requests = connectionService.getAllRequests();
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "All requests retrieved successfully",
                    requests));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null));
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse> getPendingRequests() {
        try {
            List<NewConnectionRequestDTO> requests = connectionService.getPendingRequests();
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Pending requests retrieved successfully",
                    requests));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getRequestById(@PathVariable Long id) {
        try {
            NewConnectionRequestDTO request = connectionService.getRequestById(id);
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Request retrieved successfully",
                    request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse> updateStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            NewConnectionRequestDTO updated = connectionService.updateStatus(id, status);
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Request status updated successfully",
                    updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null));
        }
    }
}
