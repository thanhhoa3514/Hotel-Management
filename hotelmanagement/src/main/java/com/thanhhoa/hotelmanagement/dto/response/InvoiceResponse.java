package com.thanhhoa.hotelmanagement.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record InvoiceResponse(
                Long id,
                String invoiceNumber,
                Long reservationId,
                Long paymentId,
                UUID keycloakUserId,
                LocalDateTime issueDate,
                BigDecimal totalAmount,
                BigDecimal tax,
                BigDecimal discount,
                BigDecimal finalAmount,
                List<InvoiceDetailResponse> details,
                LocalDateTime createdAt) {
}
