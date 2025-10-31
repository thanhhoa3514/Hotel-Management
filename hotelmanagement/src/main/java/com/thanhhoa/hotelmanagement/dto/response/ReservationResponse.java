package com.thanhhoa.hotelmanagement.dto.response;

import com.thanhhoa.hotelmanagement.entity.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ReservationResponse(
                Long id,
                GuestResponse guest,
                List<RoomResponse> rooms,
                LocalDate checkIn,
                LocalDate checkOut,
                BigDecimal totalAmount,
                ReservationStatus status,
                List<ServiceResponse> services,
                LocalDateTime createdAt,
                LocalDateTime updatedAt) {
}
