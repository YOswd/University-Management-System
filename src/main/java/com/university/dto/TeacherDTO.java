package com.university.dto;

import jakarta.validation.constraints.NotBlank;

public record TeacherDTO(Integer id, @NotBlank String name, @NotBlank String pos, @NotBlank String dept) {
}
