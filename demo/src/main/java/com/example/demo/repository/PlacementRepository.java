package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Placement;

public interface PlacementRepository extends JpaRepository<Placement, Long> {

    // Find placements by year
    List<Placement> findByYear(int year);

    // Find by qualification
    List<Placement> findByQualification(String qualification);

    List<Placement> findByQualificationIgnoreCase(String qualification);
}
