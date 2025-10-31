package com.thanhhoa.hotelmanagement.service.interfaces;

import com.thanhhoa.hotelmanagement.dto.response.InvoiceResponse;

import java.util.List;

/**
 * Service interface for Invoice management
 * Invoices are typically auto-generated, so fewer write operations
 */
public interface IInvoiceService {

    InvoiceResponse generateInvoice(Long reservationId, Long staffId);

    InvoiceResponse getInvoiceById(Long id);

    InvoiceResponse getInvoiceByNumber(String invoiceNumber);

    InvoiceResponse getInvoiceByPaymentId(Long paymentId);

    List<InvoiceResponse> getInvoicesByReservationId(Long reservationId);

    List<InvoiceResponse> getAllInvoices();

    byte[] generateInvoicePdf(Long invoiceId);
}
