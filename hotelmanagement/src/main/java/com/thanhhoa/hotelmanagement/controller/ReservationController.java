package com.thanhhoa.hotelmanagement.controller;

import com.thanhhoa.hotelmanagement.dto.request.ReservationRequest;
import com.thanhhoa.hotelmanagement.dto.response.ApiResponse;
import com.thanhhoa.hotelmanagement.dto.response.ReservationResponse;
import com.thanhhoa.hotelmanagement.entity.ReservationStatus;
import com.thanhhoa.hotelmanagement.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservation Management", description = "APIs for managing room reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @Operation(summary = "Create a new reservation")
    public ResponseEntity<ApiResponse<ReservationResponse>> createReservation(
            @Valid @RequestBody ReservationRequest request) {
        ReservationResponse response = reservationService.createReservation(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Reservation created successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get reservation by ID")
    public ResponseEntity<ApiResponse<ReservationResponse>> getReservationById(@PathVariable Long id) {
        ReservationResponse response = reservationService.getReservationById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "Get all reservations")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getAllReservations() {
        List<ReservationResponse> response = reservationService.getAllReservations();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/guest/{guestId}")
    @Operation(summary = "Get reservations by guest ID")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getReservationsByGuestId(
            @PathVariable UUID guestId) {
        List<ReservationResponse> response = reservationService.getReservationsByGuestId(guestId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get reservations by status")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getReservationsByStatus(
            @PathVariable ReservationStatus status) {
        List<ReservationResponse> response = reservationService.getReservationsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // @PutMapping("/{id}")
    // @Operation(summary = "Update reservation")
    // public ResponseEntity<ApiResponse<ReservationResponse>> updateReservation(
    // @PathVariable UUID keycloakUserId,
    // @Valid @RequestBody ReservationRequest request) {
    // ReservationResponse response =
    // reservationService.updateReservation(keycloakUserId, request);
    // return ResponseEntity.ok(ApiResponse.success("Reservation updated
    // successfully", response));
    // }

    // @PatchMapping("/{id}/cancel")
    // @Operation(summary = "Cancel reservation")
    // public ResponseEntity<ApiResponse<ReservationResponse>>
    // cancelReservation(@PathVariable Long id) {
    // ReservationResponse response = reservationService.cancelReservation(id);
    // return ResponseEntity.ok(ApiResponse.success("Reservation cancelled
    // successfully", response));
    // }

    @PatchMapping("/{id}/check-in")
    @Operation(summary = "Check in reservation")
    public ResponseEntity<ApiResponse<ReservationResponse>> checkIn(@PathVariable Long id) {
        ReservationResponse response = reservationService.checkIn(id);
        return ResponseEntity.ok(ApiResponse.success("Checked in successfully", response));
    }

    // @PatchMapping("/{id}/check-out")
    // @Operation(summary = "Check out reservation")
    // public ResponseEntity<ApiResponse<ReservationResponse>>
    // checkOut(@PathVariable UUID keycloakUserId) {
    // ReservationResponse response = reservationService.checkOut(keycloakUserId);
    // return ResponseEntity.ok(ApiResponse.success("Checked out successfully",
    // response));
    // }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete reservation")
    public ResponseEntity<ApiResponse<Void>> deleteReservation(@PathVariable UUID keycloakUserId) {
        reservationService.deleteReservation(keycloakUserId);
        return ResponseEntity.ok(ApiResponse.success("Reservation deleted successfully", null));
    }
}
