package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Entity
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Student name is required")
    @Size(max = 100, message = "Student name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Qualification is required")
    @Size(max = 100, message = "Qualification must be at most 100 characters")
    private String qualification;

    @NotBlank(message = "Course is required")
    @Size(max = 100, message = "Course must be at most 100 characters")
    private String course;

    @Min(value = 2000, message = "Year must be 2000 or later")
    private int year;

    @NotNull(message = "Hall ticket number is required")
    @Column(unique = true)
    private Long hallTicketNo;
}