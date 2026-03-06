package com.skala.process_monitoring_ai.domain.user.dto;

import com.skala.process_monitoring_ai.domain.user.entity.User;

public record UserResponse(
        Long id,
        String email,
        String name,
        String role
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole().name()
        );
    }
}
