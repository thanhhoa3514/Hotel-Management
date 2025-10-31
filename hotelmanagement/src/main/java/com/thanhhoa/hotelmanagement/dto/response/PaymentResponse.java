package com.thanhhoa.hotelmanagement.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long reservationId,
        BigDecimal amount,
        String method,
        String transactionCode,
        LocalDateTime paymentDate) {
}
