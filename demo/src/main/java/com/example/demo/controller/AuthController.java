package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService service;

    public AuthController(UserService service) {
        this.service = service;
    }

    // Register
    @PostMapping("/register")
    public User register(@Valid @RequestBody User user) {
        if ("ADMIN".equalsIgnoreCase(user.getType())) {
            throw new RuntimeException("Admin registration not allowed");
        }
        return service.register(user);
    }

    // Login
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return service.login(user.getName(), user.getPassword());
    }

    @GetMapping("/verify")
    public String verify(@RequestParam String token) {
        return service.verifyEmail(token);
    }
}