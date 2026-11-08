package com.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.university.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    
}
