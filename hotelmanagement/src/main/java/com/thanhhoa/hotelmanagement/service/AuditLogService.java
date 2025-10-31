package com.thanhhoa.hotelmanagement.service;

import com.thanhhoa.hotelmanagement.dto.response.AuditLogResponse;
import com.thanhhoa.hotelmanagement.entity.AuditLog;
import com.thanhhoa.hotelmanagement.entity.AuditStatus;
import com.thanhhoa.hotelmanagement.exception.ResourceNotFoundException;
import com.thanhhoa.hotelmanagement.mapper.EntityMapper;
import com.thanhhoa.hotelmanagement.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final EntityMapper mapper;

    public AuditLogResponse getAuditLogById(Long id) {
        log.debug("Fetching audit log with ID: {}", id);
        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AuditLog", "id", id));
        return mapper.toResponse(auditLog);
    }

    public List<AuditLogResponse> getAllAuditLogs() {
        log.debug("Fetching all audit logs");
        return auditLogRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<AuditLogResponse> getAuditLogsByUserId(Long userId) {
        log.debug("Fetching audit logs for user ID: {}", userId);
        return auditLogRepository.findByUserId(UUID.fromString(userId.toString())).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<AuditLogResponse> getAuditLogsByEntity(String entity) {
        log.debug("Fetching audit logs for entity: {}", entity);
        return auditLogRepository.findByEntity(entity).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<AuditLogResponse> getAuditLogsByEntityAndId(String entity, Long entityId) {
        log.debug("Fetching audit logs for entity: {} with ID: {}", entity, entityId);
        return auditLogRepository.findByEntityAndEntityId(entity, entityId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<AuditLogResponse> getAuditLogsByStatus(AuditStatus status) {
        log.debug("Fetching audit logs with status: {}", status);
        return auditLogRepository.findByStatus(status.name()).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<AuditLogResponse> getAuditLogsByDateRange(LocalDateTime start, LocalDateTime end) {
        log.debug("Fetching audit logs between {} and {}", start, end);
        return auditLogRepository.findByTimestampBetween(start, end).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
