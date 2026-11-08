package com.university.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.university.dto.AdminDTO;
import com.university.entity.Admin;
import com.university.exception.ResourceNotFoundException;
import com.university.repository.AdminRepository;

@Service 
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public List<AdminDTO> getAllAdmins() {
        return adminRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<AdminDTO> getAdmin(int id) {
        return adminRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public AdminDTO createAdmin(AdminDTO adminDTO) {
        Admin admin = convertToEntity(adminDTO);
        Admin createdAdmin = adminRepository.save(admin);
        return convertToDTO(createdAdmin);
    }

    @Override
    public AdminDTO updateAdmin(int id, AdminDTO adminDTO) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin", id));
        admin.setName(adminDTO.name());
        Admin updatedAdmin = adminRepository.save(admin);
        return convertToDTO(updatedAdmin);
    }

    @Override
    public void deleteAdmin(int id) {
        if (!adminRepository.existsById(id)) {
            throw new ResourceNotFoundException("Admin", id);
        }
        adminRepository.deleteById(id);
    }

    private AdminDTO convertToDTO(Admin admin) {
        return new AdminDTO(admin.getId(), admin.getName());
    }

    private Admin convertToEntity(AdminDTO adminDTO) {
        Admin admin = new Admin();
        admin.setName(adminDTO.name());
        return admin;
    }
}
