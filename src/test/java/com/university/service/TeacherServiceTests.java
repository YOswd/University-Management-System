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

import com.university.dto.TeacherDTO;
import com.university.entity.Teacher;
import com.university.repository.TeacherRepository;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTests {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    @Test
    void createTeacherMapsSavedEntityToDto() {
        when(teacherRepository.save(any(Teacher.class))).thenAnswer(invocation -> {
            Teacher teacher = invocation.getArgument(0);
            teacher.setId(21);
            return teacher;
        });

        TeacherDTO created = teacherService.createTeacher(
                new TeacherDTO(21, "X", "Professor", "CSE"));

        assertEquals(new TeacherDTO(21, "X", "Professor", "CSE"), created);
        verify(teacherRepository).save(any(Teacher.class));
    }

    @Test
    void updateTeacherChangesFieldsAndPreservesExistingId() {
        Teacher existing = new Teacher();
        existing.setId(21);
        existing.setName("X");
        existing.setPos("Professor");
        existing.setDept("CSE");
        when(teacherRepository.findById(27)).thenReturn(Optional.of(existing));
        when(teacherRepository.save(existing)).thenReturn(existing);

        TeacherDTO updated = teacherService.updateTeacher(27,
                new TeacherDTO(99, "Y", "Professor", "EEE"));

        assertEquals(new TeacherDTO(21, "Y", "Professor", "EEE"), updated);
        assertEquals(21, existing.getId());
        verify(teacherRepository).save(existing);
    }
}
