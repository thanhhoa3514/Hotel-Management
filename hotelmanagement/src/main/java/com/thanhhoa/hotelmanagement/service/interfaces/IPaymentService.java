package com.thanhhoa.hotelmanagement.service.interfaces;

import com.thanhhoa.hotelmanagement.dto.request.PaymentRequest;
import com.thanhhoa.hotelmanagement.dto.response.PaymentResponse;

import java.util.List;

/**
 * Service interface for Payment management
 */
public interface IPaymentService {

    PaymentResponse createPayment(PaymentRequest request);

    PaymentResponse getPaymentById(Long id);

    List<PaymentResponse> getAllPayments();

    List<PaymentResponse> getPaymentsByReservationId(Long reservationId);

    List<PaymentResponse> getPaymentsByStatus(Long statusId);

    PaymentResponse updatePayment(Long id, PaymentRequest request);

    PaymentResponse completePayment(Long id);

    PaymentResponse refundPayment(Long id);

    void deletePayment(Long id);
}
