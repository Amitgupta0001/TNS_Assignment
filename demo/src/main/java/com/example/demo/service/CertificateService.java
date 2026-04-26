package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Certificate;
import com.example.demo.model.Student;
import com.example.demo.repository.CertificateRepository;
import com.example.demo.repository.StudentRepository;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository repo;

    @Autowired
    private StudentRepository studentRepository;

    public Certificate addCertificate(Certificate certificate) {
        Student student = resolveStudent(certificate);
        certificate.setStudent(student);
        return repo.save(certificate);
    }

    public Certificate updateCertificate(Long id, Certificate updatedCertificate) {
        Certificate existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found with id: " + id));

        Student student = resolveStudent(updatedCertificate);
        existing.setYear(updatedCertificate.getYear());
        existing.setFileName(updatedCertificate.getFileName());
        existing.setStudent(student);
        return repo.save(existing);
    }

    public Certificate getCertificate(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found with id: " + id));
    }

    public List<Certificate> getAllCertificates() {
        return repo.findAll();
    }

    public void deleteCertificate(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Certificate not found with id: " + id);
        }
        repo.deleteById(id);
    }

    private Student resolveStudent(Certificate certificate) {
        if (certificate.getStudent() == null || certificate.getStudent().getId() == null) {
            throw new ResourceNotFoundException("Certificate must include a valid student id");
        }
        return studentRepository.findById(certificate.getStudent().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found with id: " + certificate.getStudent().getId()));
    }
}