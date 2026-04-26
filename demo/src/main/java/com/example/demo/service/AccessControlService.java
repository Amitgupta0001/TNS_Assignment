package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.exception.AccessDeniedException;

@Service
public class AccessControlService {

    public void requireAdmin(String role) {
        if (role == null || !"ADMIN".equalsIgnoreCase(role)) {
            throw new AccessDeniedException("Access denied. ADMIN role is required.");
        }
    }
}
