package com.buildsphere.reporting.dto;

import com.buildsphere.reporting.model.Role;

public record AuthResponse(
        String token,
        Long userId,
        String fullName,
        String email,
        Role role
) {
}
