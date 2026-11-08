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
            student.setRoll(11);
            return student;
        });

        StudentDTO created = studentService.createStudent(new StudentDTO(null, "Ada Student", "Science"));

        assertEquals(new StudentDTO(11, "Ada Student", "Science"), created);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void updateStudentChangesFieldsAndPreservesExistingId() {
        Student existing = new Student();
        existing.setRoll(17);
        existing.setName("Old Name");
        existing.setDept("Old Department");
        when(studentRepository.findById(17)).thenReturn(Optional.of(existing));
        when(studentRepository.save(existing)).thenReturn(existing);

        StudentDTO updated = studentService.updateStudent(17,
                new StudentDTO(99, "New Name", "Mathematics"));

        assertEquals(new StudentDTO(17, "New Name", "Mathematics"), updated);
        assertEquals(17, existing.getRoll());
        verify(studentRepository).save(existing);
    }
}
