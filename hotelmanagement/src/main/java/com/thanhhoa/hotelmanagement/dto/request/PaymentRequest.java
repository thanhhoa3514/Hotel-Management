package com.thanhhoa.hotelmanagement.dto.request;

import com.thanhhoa.hotelmanagement.entity.PaymentStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentRequest(
        @NotNull(message = "Reservation ID is required") Long reservationId,

        @NotNull(message = "Amount is required") @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0") BigDecimal amount,

        @NotBlank(message = "Payment method is required") String method,

        String transactionCode,
        PaymentStatus status) {
}
