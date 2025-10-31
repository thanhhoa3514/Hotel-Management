package com.thanhhoa.hotelmanagement.dto.request;

import com.thanhhoa.hotelmanagement.entity.RoomStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoomRequest(
        @NotBlank(message = "Room number is required") String roomNumber,

        @NotNull(message = "Room type ID is required") Long roomTypeId,

        RoomStatus roomStatus,
        Integer floor,
        String note) {
}
