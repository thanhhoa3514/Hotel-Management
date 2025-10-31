package com.thanhhoa.hotelmanagement.repository;

import com.thanhhoa.hotelmanagement.entity.ReservationRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRoomRepository extends JpaRepository<ReservationRoom, Long> {
    List<ReservationRoom> findByReservationId(Long reservationId);

    List<ReservationRoom> findByRoomId(Long roomId);

    Optional<ReservationRoom> findByReservationIdAndRoomId(Long reservationId, Long roomId);

    void deleteByReservationIdAndRoomId(Long reservationId, Long roomId);
}
