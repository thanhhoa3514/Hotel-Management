package com.thanhhoa.hotelmanagement.controller;

import com.thanhhoa.hotelmanagement.dto.request.GuestRequest;
import com.thanhhoa.hotelmanagement.dto.response.ApiResponse;
import com.thanhhoa.hotelmanagement.dto.response.GuestResponse;
import com.thanhhoa.hotelmanagement.service.interfaces.IGuestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Guest management
 * Following Dependency Inversion - depends on IGuestService interface
 */
@RestController
@RequestMapping("/api/guests")
@RequiredArgsConstructor
@Tag(name = "Guest Management", description = "APIs for managing hotel guests")
public class GuestController {

    private final IGuestService guestService; // Depend on interface, not implementation

    @PostMapping
    @Operation(summary = "Create a new guest")
    public ResponseEntity<ApiResponse<GuestResponse>> createGuest(
            @Valid @RequestBody GuestRequest request) {
        GuestResponse response = guestService.createGuest(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Guest created successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get guest by ID")
    public ResponseEntity<ApiResponse<GuestResponse>> getGuestById(@PathVariable Long id) {
        GuestResponse response = guestService.getGuestById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get guest by email")
    public ResponseEntity<ApiResponse<GuestResponse>> getGuestByEmail(@PathVariable String email) {
        GuestResponse response = guestService.getGuestByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/keycloak/{keycloakUserId}")
    @Operation(summary = "Get guest by Keycloak user ID")
    public ResponseEntity<ApiResponse<GuestResponse>> getGuestByKeycloakUserId(
            @PathVariable UUID keycloakUserId) {
        GuestResponse response = guestService.getGuestByKeycloakUserId(keycloakUserId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "Get all guests")
    public ResponseEntity<ApiResponse<List<GuestResponse>>> getAllGuests() {
        List<GuestResponse> response = guestService.getAllGuests();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update guest")
    public ResponseEntity<ApiResponse<GuestResponse>> updateGuest(
            @PathVariable Long id,
            @Valid @RequestBody GuestRequest request) {
        GuestResponse response = guestService.updateGuest(id, request);
        return ResponseEntity.ok(ApiResponse.success("Guest updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete guest")
    public ResponseEntity<ApiResponse<Void>> deleteGuest(@PathVariable Long id) {
        guestService.deleteGuest(id);
        return ResponseEntity.ok(ApiResponse.success("Guest deleted successfully", null));
    }
}
