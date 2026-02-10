package com.digipanchayat.controller;

import com.digipanchayat.dto.ApiResponse;
import com.digipanchayat.entity.Payment;
import com.digipanchayat.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payments")

public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    public ResponseEntity<ApiResponse> processPayment(@RequestParam Long billId,
            @RequestParam String paymentMethod) {
        try {
            Payment payment = paymentService.processPayment(billId, paymentMethod);
            return ResponseEntity.ok(new ApiResponse(true, "Payment processed successfully", payment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/consumer/{consumerId}")
    public ResponseEntity<ApiResponse> getConsumerPayments(@PathVariable Long consumerId) {
        try {
            List<Payment> payments = paymentService.getConsumerPayments(consumerId);
            return ResponseEntity.ok(new ApiResponse(true, "Payments retrieved successfully", payments));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/bill/{billId}")
    public ResponseEntity<ApiResponse> getBillPayments(@PathVariable Long billId) {
        try {
            List<Payment> payments = paymentService.getBillPayments(billId);
            return ResponseEntity.ok(new ApiResponse(true, "Payments retrieved successfully", payments));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}
