package com.thanhhoa.hotelmanagement.repository;

import com.thanhhoa.hotelmanagement.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByUserId(UUID userId);

    List<AuditLog> findByEntity(String entity);

    List<AuditLog> findByEntityAndEntityId(String entity, Long entityId);

    List<AuditLog> findByStatus(String status);

    List<AuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
