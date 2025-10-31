package com.thanhhoa.hotelmanagement.dto.response;

import java.time.LocalDateTime;

public record RoomImageResponse(
        Long id,
        Long roomId,
        String imageUrl,
        String description,
        Boolean isPrimary,
        Integer displayOrder,
        LocalDateTime createdAt) {
}
