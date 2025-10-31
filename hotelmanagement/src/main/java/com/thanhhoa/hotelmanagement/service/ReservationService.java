package com.thanhhoa.hotelmanagement.service;

import com.thanhhoa.hotelmanagement.audit.Auditable;
import com.thanhhoa.hotelmanagement.dto.request.ReservationRequest;
import com.thanhhoa.hotelmanagement.dto.response.ReservationResponse;
import com.thanhhoa.hotelmanagement.entity.*;
import com.thanhhoa.hotelmanagement.exception.BusinessException;
import com.thanhhoa.hotelmanagement.exception.ResourceNotFoundException;
import com.thanhhoa.hotelmanagement.mapper.EntityMapper;
import com.thanhhoa.hotelmanagement.repository.GuestRepository;
import com.thanhhoa.hotelmanagement.repository.ReservationRepository;
import com.thanhhoa.hotelmanagement.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    private final EntityMapper mapper;

    @Auditable(action = "CREATE", entity = "RESERVATION")
    public ReservationResponse createReservation(ReservationRequest request) {
        log.debug("Creating reservation for keycloak user ID: {}", request.keycloakUserId());

        Guest guest = guestRepository.findByKeycloakUserId(request.keycloakUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Guest", "keycloakUserId", request.keycloakUserId()));

        if (request.checkIn().isAfter(request.checkOut())) {
            throw new BusinessException("Check-in date must be before check-out date");
        }

        List<ReservationRoom> reservations = reservationRepository.findByRoomId(request.roomIds().get(0));

        long days = ChronoUnit.DAYS.between(request.checkIn(), request.checkOut());
        List<Room> result = new ArrayList<>(request.roomIds().size());
        for (Long roomId : request.roomIds()) {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new ResourceNotFoundException("Room", "id", roomId));
            room.setRoomStatus(RoomStatus.RESERVED);
            roomRepository.save(room);
            result.add(room);
        }
        if (result.isEmpty()) {
            throw new BusinessException("No rooms found");
        }

        Reservation reservation = Reservation.builder()
                .guest(guest)
                .checkIn(request.checkIn())
                .checkOut(request.checkOut())
                .status(request.status() != null ? request.status() : ReservationStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();
        Reservation savedReservation = reservationRepository.save(reservation);
        return mapper.toResponse(savedReservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getAllReservations() {
        log.debug("Fetching all reservations");
        return reservationRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationsByGuestId(UUID keycloakUserId) {
        log.debug("Fetching reservations for keycloak user ID: {}", keycloakUserId);
        return reservationRepository.findByKeycloakUserId(keycloakUserId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationsByStatus(ReservationStatus status) {
        log.debug("Fetching reservations with status: {}", status);
        return reservationRepository.findByReservationStatus(status).stream()
                .map(ReservationRoom::getReservation)
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    // @Auditable(action = "UPDATE", entity = "RESERVATION")
    // public ReservationResponse updateReservation(Long roomId, ReservationRequest
    // request) {
    // log.debug("Updating reservation with user ID: {}", request.keycloakUserId());
    // log.debug("Updating reservation with room ID: {}", roomId);

    // Reservation reservation =
    // reservationRepository.findByKeycloakUserId(request.keycloakUserId())
    // .orElseThrow(() -> new ResourceNotFoundException("Reservation",
    // "keycloakUserId", keycloakUserId));

    // ReservationRoom reservationRoom = reservationRepository.findByRoomId(roomId)
    // .orElseThrow(() -> new ResourceNotFoundException("ReservationRoom", "roomId",
    // roomId));

    // if (request.checkIn().isAfter(request.checkOut())) {
    // throw new BusinessException("Check-in date must be before check-out date");
    // }

    // Room room = roomRepository.findById(request.roomIds().get(0))
    // .orElseThrow(() -> new ResourceNotFoundException("Room", "id",
    // request.roomId()));

    // long days = ChronoUnit.DAYS.between(request.checkIn(), request.checkOut());
    // BigDecimal totalPrice = room.getPrice().multiply(BigDecimal.valueOf(days));

    // reservation.setCheckIn(request.checkIn());
    // reservation.setCheckOut(request.checkOut());
    // reservation.setTotalPrice(totalPrice);
    // if (request.status() != null) {
    // reservation.setStatus(request.status());
    // }

    // Reservation updatedReservation = reservationRepository.save(reservation);
    // log.info("Reservation updated successfully with ID: {}",
    // updatedReservation.getId());
    // return mapper.toResponse(updatedReservation);
    // }

    // @Auditable(action = "CANCEL", entity = "RESERVATION")
    // public ReservationResponse cancelReservation(Long id) {
    // log.debug("Cancelling reservation with ID: {}", id);

    // Reservation reservation = reservationRepository.findById(id)
    // .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));

    // reservation.setStatus(ReservationStatus.CANCELLED);

    // Room room = reservation.getReservationRooms().get(0).getRoom();
    // room.setRoomStatus(RoomStatus.AVAILABLE);
    // roomRepository.save(room);
    // }

    @Auditable(action = "CHECK_IN", entity = "RESERVATION")
    public ReservationResponse checkIn(Long id) {
        log.debug("Checking in reservation with ID: {}", id);

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));

        if (!reservation.getStatus().equals(ReservationStatus.CONFIRMED)) {
            throw new BusinessException("Only confirmed reservations can be checked in");
        }

        if (!reservation.getCheckIn().isEqual(LocalDate.now()) &&
                !reservation.getCheckIn().isBefore(LocalDate.now())) {
            throw new BusinessException("Cannot check in before the reservation date");
        }

        reservation.setStatus(ReservationStatus.CHECKED_IN);

        Room room = reservation.getReservationRooms().get(0).getRoom();
        room.setRoomStatus(RoomStatus.OCCUPIED);
        roomRepository.save(room);

        Reservation checkedInReservation = reservationRepository.save(reservation);
        log.info("Reservation checked in successfully with ID: {}", checkedInReservation.getId());
        return mapper.toResponse(checkedInReservation);
    }

    // @Auditable(action = "CHECK_OUT", entity = "RESERVATION")
    // public ReservationResponse checkOut(UUID keycloakUserId) {
    // log.debug("Checking out reservation with keycloak user ID: {}",
    // keycloakUserId);

    // Reservation reservation =
    // reservationRepository.findByKeycloakUserId(keycloakUserId)
    // .orElseThrow(() -> new ResourceNotFoundException("Reservation",
    // "keycloakUserId", keycloakUserId));

    // if (!reservation.getStatus().equals(ReservationStatus.CHECKED_IN)) {
    // throw new BusinessException("Only checked-in reservations can be checked
    // out");
    // }

    // reservation.setStatus(ReservationStatus.CHECKED_OUT);

    // Room room = reservation.getReservationRooms().get(0).getRoom();
    // room.setRoomStatus(RoomStatus.AVAILABLE);
    // roomRepository.save(room);

    // Reservation checkedOutReservation = reservationRepository.save(reservation);
    // log.info("Reservation checked out successfully with ID: {}",
    // checkedOutReservation.getId());
    // return mapper.toResponse(checkedOutReservation);
    // }

    @Auditable(action = "DELETE", entity = "RESERVATION")
    public void deleteReservation(UUID keycloakUserId) {
        log.debug("Deleting reservation with ID: {}", keycloakUserId);

        if (!reservationRepository.existsByKeycloakUserId(keycloakUserId)) {
            throw new ResourceNotFoundException("Reservation", "id", keycloakUserId);
        }

        reservationRepository.deleteById(keycloakUserId);
        log.info("Reservation deleted successfully with ID: {}", keycloakUserId);
    }

    public ReservationResponse getReservationById(Long id) {
        log.debug("Fetching reservation with ID: {}", id);
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));
        return mapper.toResponse(reservation);
    }
}
