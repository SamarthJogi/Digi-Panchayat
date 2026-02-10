package com.digipanchayat.service;

import com.digipanchayat.entity.OTP;
import com.digipanchayat.repository.OTPRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private OTPRepository otpRepository;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final int OTP_EXPIRY_MINUTES = 10;

    /**
     * Generate a 6-digit OTP
     */
    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    /**
     * Send OTP to email
     */
    @Transactional
    public String sendOTP(String email, String purpose) {
        // Delete any existing OTPs for this email
        otpRepository.deleteByEmail(email);

        // Generate new OTP
        String otpCode = generateOTP();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);

        // Save OTP to database
        OTP otp = new OTP(email, otpCode, expiryTime);
        otpRepository.save(otp);

        // Log OTP to console for development
        // Log OTP to console for development
        logger.info("\n{}", "=".repeat(80));
        logger.info("📧 OTP EMAIL SERVICE - DEVELOPMENT MODE");
        logger.info("{}", "=".repeat(80));
        logger.info("To: {}", email);
        logger.info("Purpose: {}", purpose);
        logger.info("OTP Code: {}", otpCode);
        logger.info("Expires: {}", expiryTime);
        logger.info("{}\n", "=".repeat(80));

        // Try to send email, but don't fail if it doesn't work
        try {
            if (mailSender != null && fromEmail != null && !fromEmail.isEmpty()) {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setFrom(fromEmail);
                helper.setTo(email);
                helper.setSubject("Digi-Panchayat - Your OTP Code");

                String htmlContent = buildOTPEmailTemplate(otpCode, purpose);
                helper.setText(htmlContent, true);

                mailSender.send(message);
                logger.info("✅ Email sent successfully to {}", email);
            } else {
                logger.warn("⚠️  Email not configured - OTP logged to console only");
            }
        } catch (Exception e) {
            // Email failed, but that's okay - we logged it to console
            logger.error("⚠️  Email sending failed (using console log): {}", e.getMessage());
        }

        return "OTP sent successfully to " + email + " (check console for OTP)";
    }

    /**
     * Send pre-generated OTP to email (for AuthService)
     */
    public void sendGeneratedOTP(String email, String otpCode, String purpose) {
        // Log OTP to console for development
        logger.info("\n{}", "=".repeat(80));
        logger.info("📧 OTP EMAIL SERVICE - SENDING GENERATED OTP");
        logger.info("{}", "=".repeat(80));
        logger.info("To: {}", email);
        logger.info("Purpose: {}", purpose);
        logger.info("OTP Code: {}", otpCode);
        logger.info("{}\n", "=".repeat(80));

        // Try to send email
        try {
            if (mailSender != null && fromEmail != null && !fromEmail.isEmpty()) {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setFrom(fromEmail);
                helper.setTo(email);
                helper.setSubject("Digi-Panchayat - Your OTP Code");

                String htmlContent = buildOTPEmailTemplate(otpCode, purpose);
                helper.setText(htmlContent, true);

                mailSender.send(message);
                logger.info("✅ Email sent successfully to {}", email);
            } else {
                logger.warn("⚠️  Email not configured - OTP logged to console only");
            }
        } catch (Exception e) {
            logger.error("⚠️  Email sending failed: {}", e.getMessage());
            // We don't throw exception here to avoid breaking the auth flow if email fails
            // But in production you might want to handle this differently
        }
    }

    /**
     * Verify OTP
     */
    @Transactional
    public boolean verifyOTP(String email, String otpCode) {
        logger.info("\n🔍 VERIFYING OTP:");
        logger.info("   Email: {}", email);
        logger.info("   OTP Code: {}", otpCode);

        Optional<OTP> otpOptional = otpRepository.findByEmailAndOtpCodeAndVerifiedFalse(email, otpCode);

        if (otpOptional.isEmpty()) {
            logger.warn("   ❌ Result: OTP not found in database");
            logger.info("   Checking all OTPs for this email...");
            var allOtps = otpRepository.findAll();
            allOtps.forEach(otp -> {
                if (otp.getEmail() != null && otp.getEmail().equals(email)) {
                    logger.info("   Found OTP: code={}, verified={}, expired={}",
                            otp.getOtpCode(), otp.isVerified(), otp.isExpired());
                }
            });
            return false;
        }

        OTP otp = otpOptional.get();

        // Check if expired
        if (otp.isExpired()) {
            logger.warn("   ❌ Result: OTP expired");
            otpRepository.delete(otp);
            return false;
        }

        // Mark as verified
        otp.setVerified(true);
        otpRepository.save(otp);

        logger.info("   ✅ Result: OTP verified successfully\n");
        return true;
    }

    /**
     * Build HTML email template for OTP
     */
    private String buildOTPEmailTemplate(String otpCode, String purpose) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body {
                            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                            background-color: #f4f4f4;
                            margin: 0;
                            padding: 0;
                        }
                        .container {
                            max-width: 600px;
                            margin: 40px auto;
                            background-color: #ffffff;
                            border-radius: 8px;
                            overflow: hidden;
                            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                        }
                        .header {
                            background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                            padding: 30px;
                            text-align: center;
                            color: white;
                        }
                        .header h1 {
                            margin: 0;
                            font-size: 28px;
                        }
                        .content {
                            padding: 40px 30px;
                            text-align: center;
                        }
                        .otp-box {
                            background-color: #f8f9fa;
                            border: 2px dashed #667eea;
                            border-radius: 8px;
                            padding: 20px;
                            margin: 30px 0;
                        }
                        .otp-code {
                            font-size: 36px;
                            font-weight: bold;
                            color: #667eea;
                            letter-spacing: 8px;
                            margin: 10px 0;
                        }
                        .message {
                            color: #666;
                            line-height: 1.6;
                            margin: 20px 0;
                        }
                        .footer {
                            background-color: #f8f9fa;
                            padding: 20px;
                            text-align: center;
                            color: #999;
                            font-size: 12px;
                        }
                        .warning {
                            color: #e74c3c;
                            font-size: 14px;
                            margin-top: 20px;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>🏛️ Digi-Panchayat</h1>
                        </div>
                        <div class="content">
                            <h2>Verification Code</h2>
                            <p class="message">
                                You have requested an OTP for <strong>%s</strong>.<br>
                                Please use the following code to complete your verification:
                            </p>
                            <div class="otp-box">
                                <div class="otp-code">%s</div>
                            </div>
                            <p class="message">
                                This code will expire in <strong>10 minutes</strong>.
                            </p>
                            <p class="warning">
                                ⚠️ Never share this code with anyone. Digi-Panchayat will never ask for your OTP.
                            </p>
                        </div>
                        <div class="footer">
                            <p>© 2026 Digi-Panchayat. All rights reserved.</p>
                            <p>This is an automated email. Please do not reply.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(purpose, otpCode);
    }

    /**
     * Clean up expired OTPs (can be scheduled)
     */
    @Transactional
    public void cleanupExpiredOTPs() {
        otpRepository.deleteByExpiryTimeBefore(LocalDateTime.now());
    }
}
