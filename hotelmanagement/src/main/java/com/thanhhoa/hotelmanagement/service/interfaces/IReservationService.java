package com.thanhhoa.hotelmanagement.service.interfaces;

import com.thanhhoa.hotelmanagement.dto.request.ReservationRequest;
import com.thanhhoa.hotelmanagement.dto.response.ReservationResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for Reservation management
 */
public interface IReservationService {

    ReservationResponse createReservation(ReservationRequest request);

    ReservationResponse getReservationById(Long id);

    List<ReservationResponse> getAllReservations();

    List<ReservationResponse> getReservationsByGuestId(Long guestId);

    List<ReservationResponse> getReservationsByStatus(Long statusId);

    List<ReservationResponse> getReservationsByDateRange(LocalDate startDate, LocalDate endDate);

    ReservationResponse updateReservation(Long id, ReservationRequest request);

    ReservationResponse updateReservationStatus(Long id, Long statusId);

    ReservationResponse addRoomToReservation(Long reservationId, Long roomId);

    void removeRoomFromReservation(Long reservationId, Long roomId);

    ReservationResponse addServiceToReservation(Long reservationId, Long serviceId, Integer quantity);

    void removeServiceFromReservation(Long reservationId, Long serviceId);

    ReservationResponse checkIn(Long id);

    ReservationResponse checkOut(Long id);

    ReservationResponse cancelReservation(Long id);

    void deleteReservation(Long id);
}
