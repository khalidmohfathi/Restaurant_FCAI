//package com.example.restaurant_fcai.Services;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class EmailService {
//
//    private final JavaMailSender mailSender;
//
//    @Value("${spring.mail.username:}")
//    private String fromEmail;
//
//    @Value("${app.frontend-url:http://localhost:8080}")
//    private String frontendUrl;
//
//    public void sendPasswordResetEmail(String toEmail, String resetToken) {
//        try {
//
//            if (fromEmail == null || fromEmail.trim().isEmpty()) {
//                log.warn("Email configuration not properly set up. Using mock email service.");
//                log.info("Password reset token for {}: {}", toEmail, resetToken);
//                log.info("Reset link: {}/reset-password?token={}", frontendUrl, resetToken);
//                return;
//            }
//
//            sendEmailWithSMTP(toEmail, resetToken);
//
//        } catch (Exception e) {
//            log.error("Failed to send password reset email to: {}", toEmail, e);
//
//            log.info("Password reset token for {}: {}", toEmail, resetToken);
//            log.info("Reset link: {}/reset-password?token={}", frontendUrl, resetToken);
//        }
//    }
//
//    private void sendEmailWithSMTP(String toEmail, String resetToken) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(fromEmail);
//        message.setTo(toEmail);
//        message.setSubject("Password Reset Request - Furniture Home");
//
//        String resetLink = frontendUrl + "/reset-password?token=" + resetToken;
//
//        String emailContent = String.format(
//            "Hello,\n\n" +
//            "You have requested to reset your password for your Furniture Home account.\n\n" +
//            "Please click on the following link to reset your password:\n" +
//            "%s\n\n" +
//            "This link will expire in 1 hour.\n\n" +
//            "If you did not request this password reset, please ignore this email.\n\n" +
//            "Best regards,\n" +
//            "Furniture Home Team",
//            resetLink
//        );
//
//        message.setText(emailContent);
//
//        mailSender.send(message);
//        log.info("Password reset email sent successfully to: {} via SMTP", toEmail);
//    }
//}