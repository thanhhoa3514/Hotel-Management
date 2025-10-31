package com.thanhhoa.hotelmanagement.service.interfaces;

import com.thanhhoa.hotelmanagement.dto.request.RoomTypeRequest;
import com.thanhhoa.hotelmanagement.dto.response.RoomTypeResponse;

import java.util.List;

/**
 * Service interface for Room Type management
 */
public interface IRoomTypeService {

    RoomTypeResponse createRoomType(RoomTypeRequest request);

    RoomTypeResponse getRoomTypeById(Long id);

    RoomTypeResponse getRoomTypeByName(String name);

    List<RoomTypeResponse> getAllRoomTypes();

    RoomTypeResponse updateRoomType(Long id, RoomTypeRequest request);

    void deleteRoomType(Long id);
}
