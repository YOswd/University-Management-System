package com.university.dto;

import jakarta.validation.constraints.NotBlank;

public record AdminDTO(Integer id, @NotBlank String name) {
}
