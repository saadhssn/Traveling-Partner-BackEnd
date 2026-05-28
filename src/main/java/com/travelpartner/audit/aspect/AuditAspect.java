package com.travelpartner.audit.aspect;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.audit.model.AuditLog;
import com.travelpartner.audit.service.AuditLogService;
import com.travelpartner.config.security.CustomUserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogService auditLogService;
    private final HttpServletRequest request;

    @AfterReturning("@annotation(auditAction)")
    public void logAction(JoinPoint joinPoint, AuditAction auditAction) {

        @Nullable Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId = null;
        String userType = "UNKNOWN";

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserPrincipal principal) {
            userId = principal.getUserId();
            userType = principal.getRole(); // or "PORTAL_USER"
        }

        String ip = request.getRemoteAddr();

        AuditLog log = AuditLog.builder()
                .userId(userId)
                .userType(userType)
                .action(auditAction.action())
                .module(auditAction.module())
                .description(auditAction.description())
                .ipAddress(ip)
                .createdAt(LocalDateTime.now())
                .build();

        auditLogService.saveAuditLog(log);
    }
}
