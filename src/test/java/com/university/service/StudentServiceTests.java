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

import com.university.dto.StudentDTO;
import com.university.entity.Student;
import com.university.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTests {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void createStudentMapsSavedEntityToDto() {
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
            Student student = invocation.getArgument(0);
            student.setRoll(2207064);
            return student;
        });

        StudentDTO created = studentService.createStudent(new StudentDTO(2207064, "Yasar", "CSE"));

        assertEquals(new StudentDTO(2207064, "Yasar", "CSE"), created);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void updateStudentChangesFieldsAndPreservesExistingId() {
        Student existing = new Student();
        existing.setRoll(2207064);
        existing.setName("Yasar");
        existing.setDept("CSE");
        when(studentRepository.findById(2207064)).thenReturn(Optional.of(existing));
        when(studentRepository.save(existing)).thenReturn(existing);

        StudentDTO updated = studentService.updateStudent(2207064,
                new StudentDTO(2207065, "Wajih", "EEE"));

        assertEquals(new StudentDTO(2207064, "Wajih", "EEE"), updated);
        assertEquals(2207064, existing.getRoll());
        verify(studentRepository).save(existing);
    }
}
