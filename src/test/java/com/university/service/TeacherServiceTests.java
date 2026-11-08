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
                new TeacherDTO(null, "Ada Teacher", "Professor", "Science"));

        assertEquals(new TeacherDTO(21, "Ada Teacher", "Professor", "Science"), created);
        verify(teacherRepository).save(any(Teacher.class));
    }

    @Test
    void updateTeacherChangesFieldsAndPreservesExistingId() {
        Teacher existing = new Teacher();
        existing.setId(27);
        existing.setName("Old Name");
        existing.setPos("Assistant");
        existing.setDept("Old Department");
        when(teacherRepository.findById(27)).thenReturn(Optional.of(existing));
        when(teacherRepository.save(existing)).thenReturn(existing);

        TeacherDTO updated = teacherService.updateTeacher(27,
                new TeacherDTO(99, "New Name", "Professor", "Mathematics"));

        assertEquals(new TeacherDTO(27, "New Name", "Professor", "Mathematics"), updated);
        assertEquals(27, existing.getId());
        verify(teacherRepository).save(existing);
    }
}
