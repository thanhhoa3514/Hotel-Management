package com.thanhhoa.hotelmanagement.audit;

import com.thanhhoa.hotelmanagement.entity.AuditLog;
import com.thanhhoa.hotelmanagement.entity.AuditStatus;
import com.thanhhoa.hotelmanagement.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;

    @AfterReturning(pointcut = "@annotation(auditable)", returning = "result")
    public void auditSuccess(JoinPoint joinPoint, Auditable auditable, Object result) {
        createAuditLog(joinPoint, auditable, AuditStatus.SUCCESS, "Operation completed successfully", result);
    }

    @AfterThrowing(pointcut = "@annotation(auditable)", throwing = "exception")
    public void auditFailure(JoinPoint joinPoint, Auditable auditable, Exception exception) {
        createAuditLog(joinPoint, auditable, AuditStatus.FAILURE,
                "Operation failed: " + exception.getMessage(), null);
    }

    private void createAuditLog(JoinPoint joinPoint, Auditable auditable,
            AuditStatus status, String description, Object result) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication != null ? authentication.getName() : "anonymous";
            String ipAddress = getClientIpAddress();

            Long entityId = extractEntityId(result);

            AuditLog auditLog = AuditLog.builder()
                    .userId(UUID.fromString(username.toString()))
                    .userRole(authentication.getAuthorities().iterator().next().getAuthority())
                    .action(auditable.action())
                    .entity(auditable.entity())
                    .entityId(entityId)
                    .description(description)
                    .status(status.name())
                    .ipAddress(ipAddress)
                    .build();

            auditLogRepository.save(auditLog);
            log.debug("Audit log created: {} {} - {}", auditable.action(), auditable.entity(), status);

        } catch (Exception e) {
            log.error("Failed to create audit log", e);
        }
    }

    private Long extractEntityId(Object result) {
        if (result == null) {
            return null;
        }

        try {
            if (result.getClass().getMethod("id") != null) {
                Object id = result.getClass().getMethod("id").invoke(result);
                if (id instanceof Long) {
                    return (Long) id;
                }
            }
        } catch (Exception e) {
            log.debug("Could not extract entity ID from result", e);
        }

        return null;
    }

    private String getClientIpAddress() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return "unknown";
        }

        HttpServletRequest request = attributes.getRequest();
        String xForwardedFor = request.getHeader("X-Forwarded-For");

        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }
}
