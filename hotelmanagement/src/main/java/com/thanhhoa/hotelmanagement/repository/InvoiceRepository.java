package com.thanhhoa.hotelmanagement.repository;

import com.thanhhoa.hotelmanagement.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    Optional<Invoice> findByPaymentId(Long paymentId);

    List<Invoice> findByReservationId(Long reservationId);

    boolean existsByInvoiceNumber(String invoiceNumber);
}
