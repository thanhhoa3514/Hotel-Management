package com.thanhhoa.hotelmanagement.dto.response;

import com.thanhhoa.hotelmanagement.entity.RoomStatus;

import java.util.List;

public record RoomResponse(
        Long id,
        String roomNumber,
        RoomTypeResponse roomType,
        RoomStatus roomStatus,
        Integer floor,
        String note,
        List<RoomImageResponse> images) {
}
