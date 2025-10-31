package com.thanhhoa.hotelmanagement.service;

import com.thanhhoa.hotelmanagement.audit.Auditable;
import com.thanhhoa.hotelmanagement.dto.request.RoomRequest;
import com.thanhhoa.hotelmanagement.dto.response.RoomResponse;
import com.thanhhoa.hotelmanagement.entity.Room;
import com.thanhhoa.hotelmanagement.entity.RoomStatus;
import com.thanhhoa.hotelmanagement.entity.RoomType;
import com.thanhhoa.hotelmanagement.exception.DuplicateResourceException;
import com.thanhhoa.hotelmanagement.exception.ResourceNotFoundException;
import com.thanhhoa.hotelmanagement.mapper.EntityMapper;
import com.thanhhoa.hotelmanagement.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;
    private final EntityMapper mapper;

    @Auditable(action = "CREATE", entity = "ROOM")
    public RoomResponse createRoom(RoomRequest request) {
        log.debug("Creating room with number: {}", request.roomNumber());

        if (roomRepository.existsByRoomNumber(request.roomNumber())) {
            throw new DuplicateResourceException("Room", "roomNumber", request.roomNumber());
        }

        Room room = mapper.toEntity(request);
        Room savedRoom = roomRepository.save(room);

        log.info("Room created successfully with ID: {}", savedRoom.getId());
        return mapper.toResponse(savedRoom);
    }

    @Transactional(readOnly = true)
    public RoomResponse getRoomById(Long id) {
        log.debug("Fetching room with ID: {}", id);
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));
        return mapper.toResponse(room);
    }

    @Transactional(readOnly = true)
    public RoomResponse getRoomByNumber(String roomNumber) {
        log.debug("Fetching room with number: {}", roomNumber);
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "roomNumber", roomNumber));
        return mapper.toResponse(room);
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getAllRooms() {
        log.debug("Fetching all rooms");
        return roomRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getRoomsByStatus(RoomStatus status) {
        log.debug("Fetching rooms with status: {}", status);
        return roomRepository.findByRoomStatus(status).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getRoomsByType(RoomType type) {
        log.debug("Fetching rooms with type: {}", type);
        return roomRepository.findByRoomTypeId(type.getId()).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getAvailableRooms() {
        log.debug("Fetching available rooms");
        return roomRepository.findByRoomStatus(RoomStatus.AVAILABLE).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Auditable(action = "UPDATE", entity = "ROOM")
    public RoomResponse updateRoom(Long id, RoomRequest request) {
        log.debug("Updating room with id: {}", id);

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));

        if (!room.getRoomNumber().equals(request.roomNumber()) &&
                roomRepository.existsByRoomNumber(request.roomNumber())) {
            throw new DuplicateResourceException("Room", "roomNumber", request.roomNumber());
        }

        room.setRoomNumber(request.roomNumber());
        room.setRoomType(RoomType.builder().id(request.roomTypeId()).build());
        room.setRoomStatus(request.roomStatus());
        room.setFloor(request.floor());
        room.setNote(request.note());

        Room updatedRoom = roomRepository.save(room);
        log.info("Room updated successfully with ID: {}", updatedRoom.getId());
        return mapper.toResponse(updatedRoom);
    }

    @Auditable(action = "UPDATE_STATUS", entity = "ROOM")
    public RoomResponse updateRoomStatus(Long id, RoomStatus status) {
        log.debug("Updating room status with ID: {} to {}", id, status);

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));

        room.setRoomStatus(status);
        Room updatedRoom = roomRepository.save(room);

        log.info("Room status updated successfully for ID: {}", updatedRoom.getId());
        return mapper.toResponse(updatedRoom);
    }

    @Auditable(action = "DELETE", entity = "ROOM")
    public void deleteRoom(Long id) {
        log.debug("Deleting room with ID: {}", id);

        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Room", "id", id);
        }

        roomRepository.deleteById(id);
        log.info("Room deleted successfully with ID: {}", id);
    }
}
