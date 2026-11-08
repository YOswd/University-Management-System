package com.university.service;

import java.util.List;
import java.util.Optional;

import com.university.dto.AdminDTO;

public interface AdminService {
    List<AdminDTO> getAllAdmins();
    Optional<AdminDTO> getAdmin(int id);
    AdminDTO createAdmin(AdminDTO admin);
    AdminDTO updateAdmin(int id, AdminDTO adminDTO);
    void deleteAdmin(int id);
}
