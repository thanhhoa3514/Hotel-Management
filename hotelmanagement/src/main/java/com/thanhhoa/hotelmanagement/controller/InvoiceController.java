package com.thanhhoa.hotelmanagement.controller;

import com.thanhhoa.hotelmanagement.dto.response.ApiResponse;
import com.thanhhoa.hotelmanagement.dto.response.InvoiceResponse;
import com.thanhhoa.hotelmanagement.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
@Tag(name = "Invoice Management", description = "APIs for managing invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/{id}")
    @Operation(summary = "Get invoice by ID")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getInvoiceById(@PathVariable Long id) {
        InvoiceResponse response = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/number/{invoiceNumber}")
    @Operation(summary = "Get invoice by invoice number")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getInvoiceByNumber(@PathVariable String invoiceNumber) {
        InvoiceResponse response = invoiceService.getInvoiceByNumber(invoiceNumber);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/payment/{paymentId}")
    @Operation(summary = "Get invoice by payment ID")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getInvoiceByPaymentId(@PathVariable Long paymentId) {
        InvoiceResponse response = invoiceService.getInvoiceByPaymentId(paymentId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "Get all invoices")
    public ResponseEntity<ApiResponse<List<InvoiceResponse>>> getAllInvoices() {
        List<InvoiceResponse> response = invoiceService.getAllInvoices();
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
