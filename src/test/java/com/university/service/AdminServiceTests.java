package com.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.university.dto.AdminDTO;
import com.university.entity.Admin;
import com.university.repository.AdminRepository;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTests {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    void createAdminMapsSavedEntityToDto() {
        when(adminRepository.save(any(Admin.class))).thenAnswer(invocation -> {
            Admin admin = invocation.getArgument(0);
            admin.setId(1);
            return admin;
        });

        AdminDTO created = adminService.createAdmin(new AdminDTO(null, "Ada Admin"));

        assertEquals(new AdminDTO(1, "Ada Admin"), created);
        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void updateAdminChangesNameAndPreservesExistingId() {
        Admin existing = new Admin();
        existing.setId(7);
        existing.setName("Old Name");
        when(adminRepository.findById(7)).thenReturn(Optional.of(existing));
        when(adminRepository.save(existing)).thenReturn(existing);

        AdminDTO updated = adminService.updateAdmin(7, new AdminDTO(99, "New Name"));

        assertEquals(new AdminDTO(7, "New Name"), updated);
        assertEquals(7, existing.getId());
        verify(adminRepository).save(existing);
    }
}
