package com.thanhhoa.hotelmanagement.repository;

import com.thanhhoa.hotelmanagement.entity.ReservationService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationServiceRepository extends JpaRepository<ReservationService, Long> {
    List<ReservationService> findByReservationId(Long reservationId);

    List<ReservationService> findByServiceId(Long serviceId);

    Optional<ReservationService> findByReservationIdAndServiceId(Long reservationId, Long serviceId);

    void deleteByReservationIdAndServiceId(Long reservationId, Long serviceId);
}
