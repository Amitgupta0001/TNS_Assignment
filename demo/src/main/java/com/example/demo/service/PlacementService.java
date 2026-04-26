package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Placement;
import com.example.demo.repository.PlacementRepository;

@Service
public class PlacementService {

    @Autowired
    private PlacementRepository repo;

    public Placement addPlacement(Placement placement) {
        return repo.save(placement);
    }

    public Placement updatePlacement(Long id, Placement updatedPlacement) {
        Placement existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Placement not found with id: " + id));

        existing.setName(updatedPlacement.getName());
        existing.setQualification(updatedPlacement.getQualification());
        existing.setYear(updatedPlacement.getYear());
        existing.setCompanyName(updatedPlacement.getCompanyName());
        existing.setPackageOffered(updatedPlacement.getPackageOffered());
        existing.setLocation(updatedPlacement.getLocation());
        return repo.save(existing);
    }

    public Placement getPlacement(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Placement not found with id: " + id));
    }

    public List<Placement> getAllPlacements() {
        return repo.findAll();
    }

    public List<Placement> getByYear(int year) {
        return repo.findByYear(year);
    }

    public List<Placement> getByQualification(String qualification) {
        return repo.findByQualificationIgnoreCase(qualification);
    }

    public void deletePlacement(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Placement not found with id: " + id);
        }
        repo.deleteById(id);
    }
}