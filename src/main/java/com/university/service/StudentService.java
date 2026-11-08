package com.university.service;

import java.util.List;
import java.util.Optional;

import com.university.dto.StudentDTO;

public interface StudentService {
    List<StudentDTO> getAllStudents();
    Optional<StudentDTO> getStudent(int id);
    StudentDTO createStudent(StudentDTO student);
    StudentDTO updateStudent(int id, StudentDTO studentDTO);
    void deleteStudent(int id);
}
