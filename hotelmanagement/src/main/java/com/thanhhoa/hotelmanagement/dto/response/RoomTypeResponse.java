package com.thanhhoa.hotelmanagement.dto.response;

import java.math.BigDecimal;

public record RoomTypeResponse(
        Long id,
        String name,
        String description,
        BigDecimal pricePerNight) {
}
