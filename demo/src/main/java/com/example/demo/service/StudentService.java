package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repo;

    // Add student
    public Student addStudent(Student student) {
        if (repo.existsByHallTicketNo(student.getHallTicketNo())) {
            throw new DuplicateResourceException("Student with this hall ticket already exists");
        }
        return repo.save(student);
    }

    public Student updateStudent(Long id, Student updatedStudent) {
        Student existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        if (repo.existsByHallTicketNoAndIdNot(updatedStudent.getHallTicketNo(), id)) {
            throw new DuplicateResourceException("Another student already uses this hall ticket");
        }

        existing.setName(updatedStudent.getName());
        existing.setQualification(updatedStudent.getQualification());
        existing.setCourse(updatedStudent.getCourse());
        existing.setYear(updatedStudent.getYear());
        existing.setHallTicketNo(updatedStudent.getHallTicketNo());
        return repo.save(existing);
    }

    // Get by ID
    public Student getStudent(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    // Get all students
    public List<Student> getAllStudents() {
        return repo.findAll();
    }

    // Search by hall ticket
    public Student getByHallTicket(Long hallTicket) {
        Student student = repo.findByHallTicketNo(hallTicket);
        if (student == null) {
            throw new ResourceNotFoundException("Student not found with hall ticket: " + hallTicket);
        }
        return student;
    }

    // Search by name
    public List<Student> getByName(String name) {
        return repo.findByName(name);
    }

    public List<Student> searchStudents(String name, String course, Integer year) {
        if (name != null && !name.isBlank()) {
            return repo.findByNameContainingIgnoreCase(name.trim());
        }
        if (course != null && !course.isBlank()) {
            return repo.findByCourseIgnoreCase(course.trim());
        }
        if (year != null) {
            return repo.findByYear(year);
        }
        return repo.findAll();
    }

    public Page<Student> getStudentsPage(Pageable pageable) {
        return repo.findAll(pageable);
    }

    // Delete student
    public void deleteStudent(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
        repo.deleteById(id);
    }
}