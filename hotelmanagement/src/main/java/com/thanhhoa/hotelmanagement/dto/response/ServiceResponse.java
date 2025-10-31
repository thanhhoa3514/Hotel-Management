package com.thanhhoa.hotelmanagement.dto.response;

import java.math.BigDecimal;

public record ServiceResponse(
        Long id,
        String name,
        String description,
        BigDecimal price) {
}
