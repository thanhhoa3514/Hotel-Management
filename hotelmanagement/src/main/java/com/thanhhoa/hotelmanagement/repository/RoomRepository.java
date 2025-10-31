package com.thanhhoa.hotelmanagement.repository;

import com.thanhhoa.hotelmanagement.entity.Room;
import com.thanhhoa.hotelmanagement.entity.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findById(Long id);

    Optional<Room> findByRoomNumber(String roomNumber);

    boolean existsByRoomNumber(String roomNumber);

    List<Room> findByRoomTypeId(Long roomTypeId);

    List<Room> findByRoomStatus(RoomStatus status);

    List<Room> findByFloor(Integer floor);

    @Query("SELECT DISTINCT r FROM Room r " +
            "WHERE r.roomStatus = 'AVAILABLE' " +
            "AND r.id NOT IN (" +
            "  SELECT rr.room.id FROM ReservationRoom rr " +
            "  WHERE rr.reservation.status NOT IN ('CANCELLED', 'CHECKED_OUT') " +
            "  AND ((rr.reservation.checkIn <= :checkOut AND rr.reservation.checkOut >= :checkIn))" +
            ")")
    List<Room> findAvailableRooms(@Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);
}
