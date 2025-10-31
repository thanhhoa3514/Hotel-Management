package com.thanhhoa.hotelmanagement.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record StaffResponse(
                UUID id,
                String fullName,
                String position,
                LocalDateTime createdAt,
                LocalDateTime updatedAt) {
}
