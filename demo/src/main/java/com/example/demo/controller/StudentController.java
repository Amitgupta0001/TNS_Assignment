package com.example.demo.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Student;
import com.example.demo.service.AccessControlService;
import com.example.demo.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService service;
    private final AccessControlService accessControlService;

    public StudentController(StudentService service, AccessControlService accessControlService) {
        this.service = service;
        this.accessControlService = accessControlService;
    }

    // Add student
    @PostMapping
    public Student addStudent(@RequestParam String role, @Valid @RequestBody Student student) {
        accessControlService.requireAdmin(role);
        return service.addStudent(student);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestParam String role, @Valid @RequestBody Student student) {
        accessControlService.requireAdmin(role);
        return service.updateStudent(id, student);
    }

    // Get by ID
    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id, @RequestParam String role) {
        accessControlService.requireAdmin(role);
        return service.getStudent(id);
    }

    // Get all
    @GetMapping
    public List<Student> getAllStudents(@RequestParam String role) {
        accessControlService.requireAdmin(role);
        return service.getAllStudents();
    }

    @GetMapping("/page")
    public Page<Student> getStudentsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam String role) {
        accessControlService.requireAdmin(role);
        Sort sort = "desc".equalsIgnoreCase(direction)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return service.getStudentsPage(pageable);
    }

    // Search by hall ticket
    @GetMapping("/hall/{hallTicket}")
    public Student getByHall(@PathVariable Long hallTicket, @RequestParam String role) {
        accessControlService.requireAdmin(role);
        return service.getByHallTicket(hallTicket);
    }

    // Search by name
    @GetMapping("/name/{name}")
    public List<Student> getByName(@PathVariable String name, @RequestParam String role) {
        accessControlService.requireAdmin(role);
        return service.getByName(name);
    }

    @GetMapping("/search")
    public List<Student> searchStudents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String course,
            @RequestParam(required = false) Integer year,
            @RequestParam String role) {
        accessControlService.requireAdmin(role);
        return service.searchStudents(name, course, year);
    }

    // Delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, @RequestParam String role) {
        accessControlService.requireAdmin(role);
        service.deleteStudent(id);
        return "Student deleted successfully";
    }
}