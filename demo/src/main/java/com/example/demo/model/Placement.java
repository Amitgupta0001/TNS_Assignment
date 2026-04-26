package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Entity
@Data
public class Placement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Candidate name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Qualification is required")
    @Size(max = 100, message = "Qualification must be at most 100 characters")
    private String qualification;

    @Min(value = 2000, message = "Year must be 2000 or later")
    private int year;

    @NotBlank(message = "Company name is required")
    @Size(max = 150, message = "Company name must be at most 150 characters")
    private String companyName;

    @NotBlank(message = "Package offered is required")
    @Size(max = 50, message = "Package offered must be at most 50 characters")
    private String packageOffered;

    @NotBlank(message = "Location is required")
    @Size(max = 150, message = "Placement location must be at most 150 characters")
    private String location;
}