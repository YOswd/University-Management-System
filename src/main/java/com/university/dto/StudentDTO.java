package com.university.dto;

import jakarta.validation.constraints.NotBlank;

public record StudentDTO(Integer id, @NotBlank String name, @NotBlank String dept) {
}
