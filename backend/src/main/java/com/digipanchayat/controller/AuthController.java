package com.digipanchayat.controller;

import com.digipanchayat.dto.*;
import com.digipanchayat.entity.Consumer;
import com.digipanchayat.service.AuthService;
import com.digipanchayat.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final AuthService authService;

    @Autowired
    private EmailService emailService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse> sendOTP(@RequestBody OTPRequest request) {
        try {
            String message = emailService.sendOTP(request.getEmail(), request.getPurpose());
            return ResponseEntity.ok(new ApiResponse(true, message, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse> verifyOTP(@RequestBody OtpVerifyRequest request) {
        try {
            // Verify OTP using AuthService which returns the Consumer object (including
            // role)
            Consumer consumer = authService.verifyOtp(request);
            if (consumer != null) {
                return ResponseEntity.ok(new ApiResponse(true, "OTP verified successfully", consumer));
            } else {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid or expired OTP", null));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        try {
            String message = authService.register(request);

            // Send OTP email after registration
            try {
                emailService.sendOTP(request.getEmail(), "Account Verification");
            } catch (Exception emailEx) {
                // Registration succeeded but email failed - log it
                System.err.println("Failed to send OTP email: " + emailEx.getMessage());
            }

            return ResponseEntity.ok(new ApiResponse(true, message, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        try {
            String message = authService.login(request);
            return ResponseEntity.ok(new ApiResponse(true, message, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/verify-otp-obsolete")
    public ResponseEntity<ApiResponse> verifyOtpOld(@RequestBody OtpVerifyRequest request) {
        try {
            Consumer consumer = authService.verifyOtp(request);
            return ResponseEntity.ok(new ApiResponse(true, "OTP verified successfully", consumer));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PutMapping("/consumer/{consumerId}/service-number")
    public ResponseEntity<ApiResponse> updateConsumerNumber(
            @PathVariable Long consumerId,
            @RequestParam String consumerNumber) {
        try {
            Consumer consumer = authService.updateConsumerNumber(consumerId, consumerNumber);
            return ResponseEntity.ok(new ApiResponse(true, "Consumer number updated successfully", consumer));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }
}
