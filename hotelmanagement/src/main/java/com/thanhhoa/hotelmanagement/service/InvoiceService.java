package com.thanhhoa.hotelmanagement.service;

import com.thanhhoa.hotelmanagement.dto.response.InvoiceResponse;
import com.thanhhoa.hotelmanagement.entity.Invoice;
import com.thanhhoa.hotelmanagement.exception.ResourceNotFoundException;
import com.thanhhoa.hotelmanagement.mapper.EntityMapper;
import com.thanhhoa.hotelmanagement.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final EntityMapper mapper;

    public InvoiceResponse getInvoiceById(Long id) {
        log.debug("Fetching invoice with ID: {}", id);
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "id", id));
        return mapper.toResponse(invoice);
    }

    public InvoiceResponse getInvoiceByNumber(String invoiceNumber) {
        log.debug("Fetching invoice with number: {}", invoiceNumber);
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "invoiceNumber", invoiceNumber));
        return mapper.toResponse(invoice);
    }

    public InvoiceResponse getInvoiceByPaymentId(Long paymentId) {
        log.debug("Fetching invoice for payment ID: {}", paymentId);
        Invoice invoice = invoiceRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "paymentId", paymentId));
        return mapper.toResponse(invoice);
    }

    public List<InvoiceResponse> getAllInvoices() {
        log.debug("Fetching all invoices");
        return invoiceRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
