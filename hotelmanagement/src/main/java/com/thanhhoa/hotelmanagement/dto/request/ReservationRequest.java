package com.thanhhoa.hotelmanagement.dto.request;

import com.thanhhoa.hotelmanagement.entity.ReservationStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ReservationRequest(
        @NotNull(message = "Keycloak user ID is required") UUID keycloakUserId,

        @NotEmpty(message = "At least one room is required") List<Long> roomIds,

        @NotNull(message = "Check-in date is required") @FutureOrPresent(message = "Check-in date must be today or in the future") LocalDate checkIn,

        @NotNull(message = "Check-out date is required") @Future(message = "Check-out date must be in the future") LocalDate checkOut,

        ReservationStatus status) {
}
