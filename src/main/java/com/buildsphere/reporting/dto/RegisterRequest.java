package com.buildsphere.reporting.dto;

import com.buildsphere.reporting.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank String fullName,
        @Email @NotBlank String email,
        @NotBlank @Size(min = 6) String password,
        @NotNull Role role
) {
}
