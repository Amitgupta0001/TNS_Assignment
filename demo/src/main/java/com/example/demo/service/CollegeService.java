package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.College;
import com.example.demo.repository.CollegeRepository;

@Service
public class CollegeService {

    @Autowired
    private CollegeRepository repo;

    public College addCollege(College college) {
        if (repo.existsByCollegeNameIgnoreCase(college.getCollegeName())) {
            throw new DuplicateResourceException("College already exists with this name");
        }
        return repo.save(college);
    }

    public College updateCollege(Long id, College updatedCollege) {
        College existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("College not found with id: " + id));

        if (repo.existsByCollegeNameIgnoreCaseAndIdNot(updatedCollege.getCollegeName(), id)) {
            throw new DuplicateResourceException("Another college already uses this name");
        }

        existing.setCollegeName(updatedCollege.getCollegeName());
        existing.setLocation(updatedCollege.getLocation());
        return repo.save(existing);
    }

    public College getCollege(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("College not found with id: " + id));
    }

    public List<College> getAllColleges() {
        return repo.findAll();
    }

    public void deleteCollege(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("College not found with id: " + id);
        }
        repo.deleteById(id);
    }
}