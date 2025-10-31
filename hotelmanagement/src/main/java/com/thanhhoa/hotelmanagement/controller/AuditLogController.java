package com.thanhhoa.hotelmanagement.controller;

import com.thanhhoa.hotelmanagement.dto.response.ApiResponse;
import com.thanhhoa.hotelmanagement.dto.response.AuditLogResponse;
import com.thanhhoa.hotelmanagement.entity.AuditStatus;
import com.thanhhoa.hotelmanagement.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
@Tag(name = "Audit Log Management", description = "APIs for viewing audit logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping("/{id}")
    @Operation(summary = "Get audit log by ID")
    public ResponseEntity<ApiResponse<AuditLogResponse>> getAuditLogById(@PathVariable Long id) {
        AuditLogResponse response = auditLogService.getAuditLogById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "Get all audit logs")
    public ResponseEntity<ApiResponse<List<AuditLogResponse>>> getAllAuditLogs() {
        List<AuditLogResponse> response = auditLogService.getAllAuditLogs();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get audit logs by user ID")
    public ResponseEntity<ApiResponse<List<AuditLogResponse>>> getAuditLogsByUserId(@PathVariable Long userId) {
        List<AuditLogResponse> response = auditLogService.getAuditLogsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/entity/{entity}")
    @Operation(summary = "Get audit logs by entity")
    public ResponseEntity<ApiResponse<List<AuditLogResponse>>> getAuditLogsByEntity(@PathVariable String entity) {
        List<AuditLogResponse> response = auditLogService.getAuditLogsByEntity(entity);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/entity/{entity}/{entityId}")
    @Operation(summary = "Get audit logs by entity and entity ID")
    public ResponseEntity<ApiResponse<List<AuditLogResponse>>> getAuditLogsByEntityAndId(
            @PathVariable String entity,
            @PathVariable Long entityId) {
        List<AuditLogResponse> response = auditLogService.getAuditLogsByEntityAndId(entity, entityId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get audit logs by status")
    public ResponseEntity<ApiResponse<List<AuditLogResponse>>> getAuditLogsByStatus(
            @PathVariable AuditStatus status) {
        List<AuditLogResponse> response = auditLogService.getAuditLogsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get audit logs by date range")
    public ResponseEntity<ApiResponse<List<AuditLogResponse>>> getAuditLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<AuditLogResponse> response = auditLogService.getAuditLogsByDateRange(start, end);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
