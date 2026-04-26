package com.example.demo.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.repository.CertificateRepository;
import com.example.demo.repository.CollegeRepository;
import com.example.demo.repository.PlacementRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AccessControlService;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final StudentRepository studentRepository;
    private final PlacementRepository placementRepository;
    private final CollegeRepository collegeRepository;
    private final CertificateRepository certificateRepository;
    private final UserRepository userRepository;
    private final AccessControlService accessControlService;

    public DashboardController(
            StudentRepository studentRepository,
            PlacementRepository placementRepository,
            CollegeRepository collegeRepository,
            CertificateRepository certificateRepository,
            UserRepository userRepository,
            AccessControlService accessControlService) {
        this.studentRepository = studentRepository;
        this.placementRepository = placementRepository;
        this.collegeRepository = collegeRepository;
        this.certificateRepository = certificateRepository;
        this.userRepository = userRepository;
        this.accessControlService = accessControlService;
    }

    @GetMapping("/stats")
    public Map<String, Long> getStats(@RequestParam String role) {
        accessControlService.requireAdmin(role);
        Map<String, Long> stats = new LinkedHashMap<>();
        stats.put("students", studentRepository.count());
        stats.put("placements", placementRepository.count());
        stats.put("colleges", collegeRepository.count());
        stats.put("certificates", certificateRepository.count());
        stats.put("users", userRepository.count());
        return stats;
    }
}
