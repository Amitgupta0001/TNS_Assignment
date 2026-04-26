package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // Search by hall ticket
    Student findByHallTicketNo(Long hallTicketNo);

    // Search by name (for search module)
    List<Student> findByName(String name);

    List<Student> findByNameContainingIgnoreCase(String name);

    List<Student> findByCourseIgnoreCase(String course);

    List<Student> findByYear(int year);

    boolean existsByHallTicketNo(Long hallTicketNo);

    boolean existsByHallTicketNoAndIdNot(Long hallTicketNo, Long id);
}