package com.university.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.university.dto.TeacherDTO;
import com.university.entity.Teacher;
import com.university.exception.ResourceNotFoundException;
import com.university.repository.TeacherRepository;

@Service 
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<TeacherDTO> getTeacher(int id) {
        return teacherRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public TeacherDTO createTeacher(TeacherDTO teacherDTO) {
        Teacher teacher = convertToEntity(teacherDTO);
       Teacher createdTeacher = teacherRepository.save(teacher);
        return convertToDTO(createdTeacher);
    }

    @Override
    public TeacherDTO updateTeacher(int id, TeacherDTO teacherDTO) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", id));
        teacher.setName(teacherDTO.name());
        teacher.setPos(teacherDTO.pos());
        teacher.setDept(teacherDTO.dept());
        Teacher updatedTeacher = teacherRepository.save(teacher);
        return convertToDTO(updatedTeacher);
    }

    @Override
    public void deleteTeacher(int id) {
        if (!teacherRepository.existsById(id)) {
            throw new ResourceNotFoundException("Teacher", id);
        }
        teacherRepository.deleteById(id);
    }

    private TeacherDTO convertToDTO(Teacher teacher) {
        return new TeacherDTO(teacher.getId(), teacher.getName(), teacher.getPos(), teacher.getDept());
    }

    private Teacher convertToEntity(TeacherDTO teacherDTO) {
        Teacher teacher = new Teacher();
        teacher.setName(teacherDTO.name());
        teacher.setPos(teacherDTO.pos());
        teacher.setDept(teacherDTO.dept());
        return teacher;
    }
}
