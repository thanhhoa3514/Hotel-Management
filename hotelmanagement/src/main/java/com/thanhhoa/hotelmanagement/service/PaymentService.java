package com.thanhhoa.hotelmanagement.service;

import com.thanhhoa.hotelmanagement.audit.Auditable;
import com.thanhhoa.hotelmanagement.dto.request.PaymentRequest;
import com.thanhhoa.hotelmanagement.dto.response.PaymentResponse;
import com.thanhhoa.hotelmanagement.entity.*;
import com.thanhhoa.hotelmanagement.exception.BusinessException;
import com.thanhhoa.hotelmanagement.exception.ResourceNotFoundException;
import com.thanhhoa.hotelmanagement.mapper.EntityMapper;
import com.thanhhoa.hotelmanagement.repository.InvoiceRepository;
import com.thanhhoa.hotelmanagement.repository.PaymentRepository;
import com.thanhhoa.hotelmanagement.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final InvoiceRepository invoiceRepository;
    private final EntityMapper mapper;

    @Auditable(action = "CREATE", entity = "PAYMENT")
    public PaymentResponse createPayment(PaymentRequest request) {
        log.debug("Creating payment for reservation ID: {}", request.reservationId());

        Reservation reservation = reservationRepository.findById(request.reservationId())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", request.reservationId()));

        if (reservation.getStatus().equals(ReservationStatus.CANCELLED)) {
            throw new BusinessException("Cannot create payment for cancelled reservation");
        }

        Payment payment = mapper.toEntity(request, reservation);
        Payment savedPayment = paymentRepository.save(payment);

        if (savedPayment.getStatus().equals(PaymentStatus.COMPLETED)) {
            reservation.setStatus(ReservationStatus.CONFIRMED);
            reservationRepository.save(reservation);

            generateInvoice(savedPayment);
        }

        log.info("Payment created successfully with ID: {}", savedPayment.getId());
        return mapper.toResponse(savedPayment);
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long id) {
        log.debug("Fetching payment with ID: {}", id);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));
        return mapper.toResponse(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {
        log.debug("Fetching all payments");
        return paymentRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByReservationId(Long reservationId) {
        log.debug("Fetching payments for reservation ID: {}", reservationId);
        return paymentRepository.findByReservationId(reservationId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByStatus(PaymentStatus status) {
        log.debug("Fetching payments with status: {}", status);
        return paymentRepository.findByStatus(status).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Auditable(action = "UPDATE", entity = "PAYMENT")
    public PaymentResponse updatePayment(Long id, PaymentRequest request) {
        log.debug("Updating payment with ID: {}", id);

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));

        PaymentStatus oldStatus = payment.getStatus();

        payment.setMethod(request.method());
        payment.setAmount(request.amount());
        if (request.status() != null) {
            payment.setStatus(request.status());
        }

        Payment updatedPayment = paymentRepository.save(payment);

        if (!oldStatus.equals(PaymentStatus.COMPLETED) &&
                updatedPayment.getStatus().equals(PaymentStatus.COMPLETED)) {

            Reservation reservation = updatedPayment.getReservation();
            reservation.setStatus(ReservationStatus.CONFIRMED);
            reservationRepository.save(reservation);

            generateInvoice(updatedPayment);
        }

        log.info("Payment updated successfully with ID: {}", updatedPayment.getId());
        return mapper.toResponse(updatedPayment);
    }

    @Auditable(action = "COMPLETE", entity = "PAYMENT")
    public PaymentResponse completePayment(Long id) {
        log.debug("Completing payment with ID: {}", id);

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));

        if (payment.getStatus().equals(PaymentStatus.COMPLETED)) {
            throw new BusinessException("Payment is already completed");
        }

        payment.setStatus(PaymentStatus.COMPLETED);
        Payment completedPayment = paymentRepository.save(payment);

        Reservation reservation = completedPayment.getReservation();
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);

        generateInvoice(completedPayment);

        log.info("Payment completed successfully with ID: {}", completedPayment.getId());
        return mapper.toResponse(completedPayment);
    }

    private void generateInvoice(Payment payment) {
        if (invoiceRepository.findByPaymentId(payment.getId()).isPresent()) {
            log.debug("Invoice already exists for payment ID: {}", payment.getId());
            return;
        }

        String invoiceNumber = generateInvoiceNumber();

        Invoice invoice = Invoice.builder()
                .payment(payment)
                .invoiceNumber(invoiceNumber)
                .totalAmount(payment.getAmount())
                .build();

        invoiceRepository.save(invoice);
        log.info("Invoice generated with number: {}", invoiceNumber);
    }

    private String generateInvoiceNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "INV-" + timestamp;
    }

    @Auditable(action = "DELETE", entity = "PAYMENT")
    public void deletePayment(Long id) {
        log.debug("Deleting payment with ID: {}", id);

        if (!paymentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payment", "id", id);
        }

        paymentRepository.deleteById(id);
        log.info("Payment deleted successfully with ID: {}", id);
    }
}
