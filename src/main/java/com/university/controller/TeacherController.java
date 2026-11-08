package com.university.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.university.dto.TeacherDTO;
import com.university.exception.ResourceNotFoundException;
import com.university.service.TeacherService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public List<TeacherDTO> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/{id}")
    public TeacherDTO getTeacher(@PathVariable int id) {
        return teacherService.getTeacher(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", id));
    }

    @PostMapping
    public ResponseEntity<TeacherDTO> createTeacher(@Valid @RequestBody TeacherDTO request) {
        TeacherDTO created = teacherService.createTeacher(request);
        return ResponseEntity.created(URI.create("/api/teachers/" + created.id())).body(created);
    }

    @PutMapping("update/{id}")
    public TeacherDTO updateTeacher(@PathVariable int id, @Valid @RequestBody TeacherDTO request) {
        return teacherService.updateTeacher(id, request);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable int id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
