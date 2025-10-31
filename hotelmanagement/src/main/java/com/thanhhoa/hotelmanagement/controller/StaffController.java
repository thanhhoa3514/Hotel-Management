package com.thanhhoa.hotelmanagement.controller;

import com.thanhhoa.hotelmanagement.dto.request.StaffRequest;
import com.thanhhoa.hotelmanagement.dto.response.ApiResponse;
import com.thanhhoa.hotelmanagement.dto.response.StaffResponse;
import com.thanhhoa.hotelmanagement.service.StaffService;
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
@RequestMapping("/api/staff")
@RequiredArgsConstructor
@Tag(name = "Staff Management", description = "APIs for managing hotel staff")
public class StaffController {

    private final StaffService staffService;

    @PostMapping
    @Operation(summary = "Create a new staff member")
    public ResponseEntity<ApiResponse<StaffResponse>> createStaff(@Valid @RequestBody StaffRequest request) {
        StaffResponse response = staffService.createStaff(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Staff created successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get staff by ID")
    public ResponseEntity<ApiResponse<StaffResponse>> getStaffById(@PathVariable UUID id) {
        StaffResponse response = staffService.getStaffById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // @GetMapping("/user/{userId}")
    // @Operation(summary = "Get staff by user ID")
    // public ResponseEntity<ApiResponse<StaffResponse>>
    // getStaffByUserId(@PathVariable UUID userId) {
    // StaffResponse response = staffService.getStaffByUserId(userId);
    // return ResponseEntity.ok(ApiResponse.success(response));
    // }

    @GetMapping
    @Operation(summary = "Get all staff")
    public ResponseEntity<ApiResponse<List<StaffResponse>>> getAllStaff() {
        List<StaffResponse> response = staffService.getAllStaff();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // @GetMapping("/department/{department}")
    // @Operation(summary = "Get staff by department")
    // public ResponseEntity<ApiResponse<List<StaffResponse>>>
    // getStaffByDepartment(@PathVariable String department) {
    // List<StaffResponse> response = staffService.getStaffByDepartment(department);
    // return ResponseEntity.ok(ApiResponse.success(response));
    // }

    // @PutMapping("/{id}")
    // @Operation(summary = "Update staff")
    // public ResponseEntity<ApiResponse<StaffResponse>> updateStaff(
    // @PathVariable Long id,
    // @Valid @RequestBody StaffRequest request) {
    // StaffResponse response = staffService.updateStaff(id, request);
    // return ResponseEntity.ok(ApiResponse.success("Staff updated successfully",
    // response));
    // }

    // @DeleteMapping("/{id}")
    // @Operation(summary = "Delete staff")
    // public ResponseEntity<ApiResponse<Void>> deleteStaff(@PathVariable Long id) {
    // staffService.deleteStaff(id);
    // return ResponseEntity.ok(ApiResponse.success("Staff deleted successfully",
    // null));
    // }
}
