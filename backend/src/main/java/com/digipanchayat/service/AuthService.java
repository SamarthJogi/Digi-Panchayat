package com.digipanchayat.service;

import com.digipanchayat.dto.LoginRequest;
import com.digipanchayat.dto.OtpVerifyRequest;
import com.digipanchayat.dto.RegisterRequest;
import com.digipanchayat.entity.Consumer;
import com.digipanchayat.repository.ConsumerRepository;
import org.springframework.stereotype.Service;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final ConsumerRepository consumerRepository;
    private final EmailService emailService;

    public AuthService(ConsumerRepository consumerRepository, EmailService emailService) {
        this.consumerRepository = consumerRepository;
        this.emailService = emailService;
    }

    private static final long OTP_EXPIRY_MINUTES = 5;

    public String register(RegisterRequest request) {
        // Check if consumer already exists
        if (consumerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        if (consumerRepository.existsByMobile(request.getMobile())) {
            throw new RuntimeException("Mobile already registered");
        }

        // Create new consumer
        Consumer consumer = new Consumer();
        consumer.setEmail(request.getEmail());
        consumer.setMobile(request.getMobile());
        consumer.setPassword(request.getPassword()); // In production, hash this
        consumer.setName(request.getName());
        consumer.setRole("CONSUMER");
        consumer.setStatus("INACTIVE"); // Will be ACTIVE after OTP verification

        // Generate OTP
        String otp = generateOTP();
        consumer.setOtp(otp);
        consumer.setOtpExpiry(System.currentTimeMillis() + (OTP_EXPIRY_MINUTES * 60 * 1000));

        consumerRepository.save(consumer);

        // Send OTP via Email
        logger.info("OTP for {}: {}", request.getEmail(), otp);
        emailService.sendGeneratedOTP(request.getEmail(), otp, "Registration Verification");

        return "Registration successful. OTP sent to your email/mobile.";
    }

    public String login(LoginRequest request) {
        Consumer consumer = consumerRepository.findByEmail(request.getEmailOrMobile())
                .or(() -> consumerRepository.findByMobile(request.getEmailOrMobile()))
                .orElseThrow(() -> new RuntimeException("Consumer not found"));

        if (!consumer.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Generate OTP for login
        String otp = generateOTP();
        consumer.setOtp(otp);
        consumer.setOtpExpiry(System.currentTimeMillis() + (OTP_EXPIRY_MINUTES * 60 * 1000));
        consumerRepository.save(consumer);

        // Send OTP via Email (if email is available)
        // Login request can be email or mobile. If it's email, send email.
        // If it's mobile, we'd need SMS service, but for now we'll check if consumer
        // has email
        if (consumer.getEmail() != null && !consumer.getEmail().isEmpty()) {
            emailService.sendGeneratedOTP(consumer.getEmail(), otp, "Login Verification");
        }

        logger.info("Login OTP for {}: {}", request.getEmailOrMobile(), otp);

        return "OTP sent to your registered email/mobile.";
    }

    public Consumer verifyOtp(OtpVerifyRequest request) {
        Consumer consumer = consumerRepository.findByEmail(request.getEmailOrMobile())
                .or(() -> consumerRepository.findByMobile(request.getEmailOrMobile()))
                .orElseThrow(() -> new RuntimeException("Consumer not found"));

        if (consumer.getOtp() == null || !consumer.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        if (consumer.getOtpExpiry() < System.currentTimeMillis()) {
            throw new RuntimeException("OTP expired");
        }

        // Clear OTP and activate consumer
        consumer.setOtp(null);
        consumer.setOtpExpiry(null);
        consumer.setStatus("ACTIVE");

        return consumerRepository.save(consumer);
    }

    public Consumer updateConsumerNumber(Long consumerId, String consumerNumber) {
        Consumer consumer = consumerRepository.findById(consumerId)
                .orElseThrow(() -> new RuntimeException("Consumer not found with ID: " + consumerId));

        consumer.setConsumerNumber(consumerNumber);
        return consumerRepository.save(consumer);
    }

    private String generateOTP() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
}
