package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.College;

public interface CollegeRepository extends JpaRepository<College, Long> {

    // Search college by name
    College findByCollegeName(String collegeName);

    boolean existsByCollegeNameIgnoreCase(String collegeName);

    boolean existsByCollegeNameIgnoreCaseAndIdNot(String collegeName, Long id);
}