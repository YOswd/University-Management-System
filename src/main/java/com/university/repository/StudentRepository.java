package com.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.university.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    
}
