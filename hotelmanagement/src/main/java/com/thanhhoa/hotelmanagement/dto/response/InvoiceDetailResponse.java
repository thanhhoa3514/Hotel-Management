package com.thanhhoa.hotelmanagement.dto.response;

import java.math.BigDecimal;

public record InvoiceDetailResponse(
        Long id,
        String itemType,
        Long itemId,
        String description,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice) {
}
