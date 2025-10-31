package com.thanhhoa.hotelmanagement.service.interfaces;

import com.thanhhoa.hotelmanagement.dto.response.AuditLogResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for Audit Log management
 * Read-only operations for security compliance
 */
public interface IAuditLogService {

    AuditLogResponse getAuditLogById(Long id);

    List<AuditLogResponse> getAllAuditLogs();

    List<AuditLogResponse> getAuditLogsByUserId(UUID userId);

    List<AuditLogResponse> getAuditLogsByEntity(String entity);

    List<AuditLogResponse> getAuditLogsByEntityAndId(String entity, Long entityId);

    List<AuditLogResponse> getAuditLogsByStatus(String status);

    List<AuditLogResponse> getAuditLogsByDateRange(LocalDateTime start, LocalDateTime end);
}
