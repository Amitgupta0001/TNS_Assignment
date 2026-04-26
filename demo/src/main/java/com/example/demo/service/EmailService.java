package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String verificationBaseUrl;

    public EmailService(JavaMailSender mailSender,
            @Value("${app.verification.base-url}") String verificationBaseUrl) {
        this.mailSender = mailSender;
        this.verificationBaseUrl = verificationBaseUrl;
    }

    public void sendVerificationEmail(String toEmail, String token) {
        String link = verificationBaseUrl + "/auth/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Verify your account");
        message.setText("Click the following link to verify your account: " + link);

        mailSender.send(message);
    }
}
