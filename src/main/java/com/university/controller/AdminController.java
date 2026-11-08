package com.university.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.university.dto.AdminDTO;
import com.university.exception.ResourceNotFoundException;
import com.university.service.AdminService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admins")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public List<AdminDTO> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    @GetMapping("/{id}")
    public AdminDTO getAdmin(@PathVariable int id) {
        return adminService.getAdmin(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin", id));
    }

    @PostMapping
    public ResponseEntity<AdminDTO> createAdmin(@Valid @RequestBody AdminDTO request) {
        AdminDTO created = adminService.createAdmin(request);
        return ResponseEntity.created(URI.create("/api/admins/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public AdminDTO updateAdmin(@PathVariable int id, @Valid @RequestBody AdminDTO request) {
        return adminService.updateAdmin(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable int id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
