package com.thanhhoa.hotelmanagement.controller;

import com.thanhhoa.hotelmanagement.dto.request.PaymentRequest;
import com.thanhhoa.hotelmanagement.dto.response.ApiResponse;
import com.thanhhoa.hotelmanagement.dto.response.PaymentResponse;
import com.thanhhoa.hotelmanagement.entity.PaymentStatus;
import com.thanhhoa.hotelmanagement.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment Management", description = "APIs for managing payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Operation(summary = "Create a new payment")
    public ResponseEntity<ApiResponse<PaymentResponse>> createPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Payment created successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentById(@PathVariable Long id) {
        PaymentResponse response = paymentService.getPaymentById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "Get all payments")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getAllPayments() {
        List<PaymentResponse> response = paymentService.getAllPayments();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/reservation/{reservationId}")
    @Operation(summary = "Get payments by reservation ID")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getPaymentsByReservationId(
            @PathVariable Long reservationId) {
        List<PaymentResponse> response = paymentService.getPaymentsByReservationId(reservationId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get payments by status")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getPaymentsByStatus(
            @PathVariable PaymentStatus status) {
        List<PaymentResponse> response = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update payment")
    public ResponseEntity<ApiResponse<PaymentResponse>> updatePayment(
            @PathVariable Long id,
            @Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.updatePayment(id, request);
        return ResponseEntity.ok(ApiResponse.success("Payment updated successfully", response));
    }

    @PatchMapping("/{id}/complete")
    @Operation(summary = "Complete payment")
    public ResponseEntity<ApiResponse<PaymentResponse>> completePayment(@PathVariable Long id) {
        PaymentResponse response = paymentService.completePayment(id);
        return ResponseEntity.ok(ApiResponse.success("Payment completed successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete payment")
    public ResponseEntity<ApiResponse<Void>> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok(ApiResponse.success("Payment deleted successfully", null));
    }
}
