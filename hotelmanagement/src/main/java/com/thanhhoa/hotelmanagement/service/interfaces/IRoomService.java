package com.thanhhoa.hotelmanagement.service.interfaces;

import com.thanhhoa.hotelmanagement.dto.request.RoomRequest;
import com.thanhhoa.hotelmanagement.dto.response.RoomResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for Room management
 */
public interface IRoomService {

    RoomResponse createRoom(RoomRequest request);

    RoomResponse getRoomById(Long id);

    RoomResponse getRoomByNumber(String roomNumber);

    List<RoomResponse> getAllRooms();

    List<RoomResponse> getRoomsByType(Long roomTypeId);

    List<RoomResponse> getRoomsByStatus(Long roomStatusId);

    List<RoomResponse> getAvailableRooms(LocalDate checkIn, LocalDate checkOut);

    List<RoomResponse> getRoomsByFloor(Integer floor);

    RoomResponse updateRoom(Long id, RoomRequest request);

    RoomResponse updateRoomStatus(Long id, Long statusId);

    void deleteRoom(Long id);
}
