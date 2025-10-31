package com.thanhhoa.hotelmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoomImageRequest(
        @NotNull(message = "Room ID is required") Long roomId,

        @NotBlank(message = "Image URL is required") String imageUrl,

        String description,
        Boolean isPrimary,
        Integer displayOrder) {
}
