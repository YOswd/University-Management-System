package com.university.service;

import java.util.List;
import java.util.Optional;

import com.university.dto.TeacherDTO;

public interface TeacherService {
    List<TeacherDTO> getAllTeachers();
    Optional<TeacherDTO> getTeacher(int id);
    TeacherDTO createTeacher(TeacherDTO teacher);
    TeacherDTO updateTeacher(int id, TeacherDTO teacherDTO);
    void deleteTeacher(int id);
}
