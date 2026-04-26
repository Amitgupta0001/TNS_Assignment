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

import com.example.demo.model.Certificate;
import com.example.demo.service.AccessControlService;
import com.example.demo.service.CertificateService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private final CertificateService service;
    private final AccessControlService accessControlService;

    public CertificateController(CertificateService service, AccessControlService accessControlService) {
        this.service = service;
        this.accessControlService = accessControlService;
    }

    @PostMapping
    public Certificate add(@RequestParam String role, @Valid @RequestBody Certificate certificate) {
        accessControlService.requireAdmin(role);
        return service.addCertificate(certificate);
    }

    @PutMapping("/{id}")
    public Certificate update(
            @PathVariable Long id,
            @RequestParam String role,
            @Valid @RequestBody Certificate certificate) {
        accessControlService.requireAdmin(role);
        return service.updateCertificate(id, certificate);
    }

    @GetMapping("/{id}")
    public Certificate get(@PathVariable Long id, @RequestParam String role) {
        accessControlService.requireAdmin(role);
        return service.getCertificate(id);
    }

    @GetMapping
    public List<Certificate> getAll(@RequestParam String role) {
        accessControlService.requireAdmin(role);
        return service.getAllCertificates();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, @RequestParam String role) {
        accessControlService.requireAdmin(role);
        service.deleteCertificate(id);
        return "Certificate deleted";
    }
}