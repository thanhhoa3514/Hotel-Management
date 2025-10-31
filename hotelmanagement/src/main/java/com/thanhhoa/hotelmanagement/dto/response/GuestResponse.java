package com.thanhhoa.hotelmanagement.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record GuestResponse(
                Long id,
                String fullName,
                UUID keycloakUserId,
                LocalDateTime createdAt,
                LocalDateTime updatedAt) {
}
