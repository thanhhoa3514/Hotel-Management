package com.thanhhoa.hotelmanagement.repository;

import com.thanhhoa.hotelmanagement.entity.Reservation;
import com.thanhhoa.hotelmanagement.entity.ReservationRoom;
import com.thanhhoa.hotelmanagement.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
        boolean existsByKeycloakUserId(UUID keycloakUserId);

        List<Reservation> findByKeycloakUserId(UUID keycloakUserId);

        List<ReservationRoom> findByRoomId(Long roomId);

        List<ReservationRoom> findByReservationStatus(ReservationStatus status);

        @Query("SELECT r FROM Reservation r WHERE r.checkIn BETWEEN :startDate AND :endDate")
        List<Reservation> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

        @Query("SELECT r FROM Reservation r " +
                        "JOIN r.reservationRooms rr " +
                        "WHERE rr.room.id = :roomId " +
                        "AND r.status NOT IN ('CANCELLED', 'CHECKED_OUT') " +
                        "AND ((r.checkIn BETWEEN :checkIn AND :checkOut) " +
                        "OR (r.checkOut BETWEEN :checkIn AND :checkOut) " +
                        "OR (r.checkIn <= :checkIn AND r.checkOut >= :checkOut))")
        List<Reservation> findConflictingReservations(
                        @Param("roomId") Long roomId,
                        @Param("checkIn") LocalDate checkIn,
                        @Param("checkOut") LocalDate checkOut);

        void deleteById(UUID keycloakUserId);
}
