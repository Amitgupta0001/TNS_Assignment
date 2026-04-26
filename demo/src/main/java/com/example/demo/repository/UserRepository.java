package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // Login check
    User findByName(String name);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByEmailIgnoreCase(String email);
}
