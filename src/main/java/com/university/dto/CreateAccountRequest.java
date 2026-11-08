package com.university.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateAccountRequest(
        @NotBlank @Pattern(regexp = "[A-Za-z0-9._-]{3,50}") String username,
        @NotBlank @Size(min = 12) String password,
        @NotBlank @Pattern(regexp = "ADMIN|TEACHER|STUDENT") String role) {
}
