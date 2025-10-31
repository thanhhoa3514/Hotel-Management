package com.thanhhoa.hotelmanagement.dto.response;

import com.thanhhoa.hotelmanagement.entity.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
                UUID keycloakUserId,
                String username,
                String email,
                UserRole role,
                LocalDateTime createdAt,
                LocalDateTime updatedAt) {
}
