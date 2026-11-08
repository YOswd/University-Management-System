package com.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.university.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    
}