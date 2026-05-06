package com.example.demo.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public Map<String, Object> root() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("project", "Vettripath - Student Placement Management System");
        response.put("status", "🟢 Running");
        response.put("version", "1.0.0");
        response.put("api_version", "v1");
        response.put("description", "Comprehensive Student Placement Management System with Spring Boot 4.0.5 & React 19");
        
        Map<String, Object> endpoints = new LinkedHashMap<>();
        endpoints.put("authentication", "/auth/register, /auth/login, /auth/verify");
        endpoints.put("students", "/students (GET, POST, PUT, DELETE)");
        endpoints.put("colleges", "/colleges (GET, POST, PUT, DELETE)");
        endpoints.put("placements", "/placements (GET, POST, PUT, DELETE)");
        endpoints.put("certificates", "/certificates (GET, POST, PUT, DELETE)");
        endpoints.put("dashboard", "/dashboard/stats");
        
        response.put("endpoints", endpoints);
        response.put("documentation", "See README.md for complete API documentation");
        response.put("frontend", "http://localhost:5173");
        response.put("timestamp", System.currentTimeMillis());
        
        return response;
    }

    @GetMapping("/api")
    public Map<String, Object> apiInfo() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("api_name", "TNS Placement Management System API");
        response.put("base_url", "http://localhost:8080");
        response.put("version", "1.0.0");
        response.put("framework", "Spring Boot 4.0.5");
        response.put("database", "PostgreSQL 16.13");
        response.put("security", "BCrypt with Role-Based Access Control");
        
        Map<String, String> features = new LinkedHashMap<>();
        features.put("authentication", "User registration, login, email verification");
        features.put("students", "Full CRUD with search and filtering");
        features.put("placements", "Placement offer management");
        features.put("colleges", "College information management");
        features.put("certificates", "Student certificate tracking");
        features.put("dashboard", "Real-time statistics and overview");
        
        response.put("features", features);
        response.put("ready", true);
        
        return response;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("service", "Vettripath API");
        response.put("timestamp", System.currentTimeMillis());
        response.put("uptime", "Active");
        
        return response;
    }
}
