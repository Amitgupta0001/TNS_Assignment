package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Placement;
import com.example.demo.service.AccessControlService;
import com.example.demo.service.PlacementService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/placements")
public class PlacementController {

    private final PlacementService service;
    private final AccessControlService accessControlService;

    public PlacementController(PlacementService service, AccessControlService accessControlService) {
        this.service = service;
        this.accessControlService = accessControlService;
    }

    @PostMapping
    public Placement addPlacement(@RequestParam String role, @Valid @RequestBody Placement placement) {
        accessControlService.requireAdmin(role);
        return service.addPlacement(placement);
    }

    @PutMapping("/{id}")
    public Placement updatePlacement(
            @PathVariable Long id,
            @RequestParam String role,
            @Valid @RequestBody Placement placement) {
        accessControlService.requireAdmin(role);
        return service.updatePlacement(id, placement);
    }

    @GetMapping("/{id}")
    public Placement getPlacement(@PathVariable Long id, @RequestParam String role) {
        accessControlService.requireAdmin(role);
        return service.getPlacement(id);
    }

    @GetMapping
    public List<Placement> getAll(@RequestParam String role) {
        accessControlService.requireAdmin(role);
        return service.getAllPlacements();
    }

    @GetMapping("/year/{year}")
    public List<Placement> getByYear(@PathVariable int year, @RequestParam String role) {
        accessControlService.requireAdmin(role);
        return service.getByYear(year);
    }

    @GetMapping("/qualification/{qualification}")
    public List<Placement> getByQualification(@PathVariable String qualification, @RequestParam String role) {
        accessControlService.requireAdmin(role);
        return service.getByQualification(qualification);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, @RequestParam String role) {
        accessControlService.requireAdmin(role);
        service.deletePlacement(id);
        return "Placement deleted";
    }
}