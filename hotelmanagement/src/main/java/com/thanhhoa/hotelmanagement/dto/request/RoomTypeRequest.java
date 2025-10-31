package com.thanhhoa.hotelmanagement.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RoomTypeRequest(
        @NotBlank(message = "Name is required") String name,

        String description,

        @NotNull(message = "Price per night is required") @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0") BigDecimal pricePerNight) {
}
