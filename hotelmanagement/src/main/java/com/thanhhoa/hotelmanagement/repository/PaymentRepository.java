package com.thanhhoa.hotelmanagement.repository;

import com.thanhhoa.hotelmanagement.entity.Payment;
import com.thanhhoa.hotelmanagement.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByReservationId(Long reservationId);

    List<Payment> findByStatus(PaymentStatus status);
}
