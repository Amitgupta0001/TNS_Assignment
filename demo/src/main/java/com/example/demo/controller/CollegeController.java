package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.College;
import com.example.demo.service.AccessControlService;
import com.example.demo.service.CollegeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/colleges")
public class CollegeController {

    private final CollegeService service;
    private final AccessControlService accessControlService;

    public CollegeController(CollegeService service, AccessControlService accessControlService) {
        this.service = service;
        this.accessControlService = accessControlService;
    }

    @PostMapping
    public College addCollege(@RequestParam String role, @Valid @RequestBody College college) {
        accessControlService.requireAdmin(role);
        return service.addCollege(college);
    }

    @PutMapping("/{id}")
    public College updateCollege(@PathVariable Long id, @RequestParam String role, @Valid @RequestBody College college) {
        accessControlService.requireAdmin(role);
        return service.updateCollege(id, college);
    }

    @GetMapping("/{id}")
    public College getCollege(@PathVariable Long id, @RequestParam String role) {
        accessControlService.requireAdmin(role);
        return service.getCollege(id);
    }

    @GetMapping
    public List<College> getAll(@RequestParam String role) {
        accessControlService.requireAdmin(role);
        return service.getAllColleges();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, @RequestParam String role) {
        accessControlService.requireAdmin(role);
        service.deleteCollege(id);
        return "College deleted";
    }
}