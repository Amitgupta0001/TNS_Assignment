package com.example.demo.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.User;
import com.example.demo.model.VerificationToken;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerificationTokenRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    // Register user
    public User register(User user) {
        if ("ADMIN".equalsIgnoreCase(user.getType())) {
            throw new RuntimeException("Admin registration not allowed");
        }
        if (repo.existsByNameIgnoreCase(user.getName())) {
            throw new DuplicateResourceException("User already exists with this name");
        }
        if (repo.existsByEmailIgnoreCase(user.getEmail())) {
            throw new DuplicateResourceException("User already exists with this email");
        }
        user.setType("USER");
        user.setEnabled(false);

        User savedUser = repo.save(user);

        tokenRepository.deleteByUserId(savedUser.getId());
        String tokenValue = UUID.randomUUID().toString();
        VerificationToken token = new VerificationToken();
        token.setToken(tokenValue);
        token.setUser(savedUser);
        tokenRepository.save(token);

        try {
            emailService.sendVerificationEmail(savedUser.getEmail(), tokenValue);
        } catch (Exception e) {
            // Email sending failed, but allow registration to proceed (development mode)
            System.out.println("Warning: Email verification could not be sent: " + e.getMessage());
        }
        return savedUser;
    }

    public String verifyEmail(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            throw new ResourceNotFoundException("Invalid verification token");
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        repo.save(user);
        tokenRepository.delete(verificationToken);
        return "Account verified successfully";
    }

    // Login check
    public String login(String name, String password) {
        User user = repo.findByName(name);

        if (user == null || !user.getPassword().equals(password)) {
            return "Invalid Credentials";
        }

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            return "Please verify your email first";
        }

        return "Login Successful";
    }
}