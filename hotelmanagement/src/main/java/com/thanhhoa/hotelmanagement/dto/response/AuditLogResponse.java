package com.thanhhoa.hotelmanagement.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuditLogResponse(
        Long id,
        UUID userId,
        String userRole,
        String action,
        String entity,
        Long entityId,
        String description,
        String status,
        String ipAddress,
        LocalDateTime timestamp) {
}
