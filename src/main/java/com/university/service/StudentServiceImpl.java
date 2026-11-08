package com.university.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.university.dto.StudentDTO;
import com.university.entity.Student;
import com.university.exception.ResourceNotFoundException;
import com.university.repository.StudentRepository;

@Service 
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<StudentDTO> getStudent(int id) {
        return studentRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        Student student = convertToEntity(studentDTO);
        Student createdStudent = studentRepository.save(student);
        return convertToDTO(createdStudent);
    }

    @Override
    public StudentDTO updateStudent(int id, StudentDTO studentDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
        student.setName(studentDTO.name());
        student.setDept(studentDTO.dept());
        Student updatedStudent = studentRepository.save(student);
        return convertToDTO(updatedStudent);
    }

    @Override
    public void deleteStudent(int id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student", id);
        }
        studentRepository.deleteById(id);
    }

    private StudentDTO convertToDTO(Student student) {
        return new StudentDTO(student.getRoll(), student.getName(), student.getDept());
    }

    private Student convertToEntity(StudentDTO studentDTO) {
        Student student = new Student();
        student.setName(studentDTO.name());
        student.setDept(studentDTO.dept());
        return student;
    }
}
