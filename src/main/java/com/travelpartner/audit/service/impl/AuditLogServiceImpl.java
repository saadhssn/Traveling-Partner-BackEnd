package com.travelpartner.audit.service.impl;

import com.travelpartner.audit.dto.AuditLogResponse;
import com.travelpartner.audit.model.AuditLog;
import com.travelpartner.audit.repository.AuditLogRepository;
import com.travelpartner.audit.service.AuditLogService;
import com.travelpartner.user.model.User;
import com.travelpartner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Override
    public void saveAuditLog(AuditLog log) {
        auditLogRepository.save(log);
    }

    @Override
    public Page<AuditLogResponse> getAllLogs(Pageable pageable) {
        return auditLogRepository.findAllWithUser(pageable);
    }

    @Override
    public AuditLog getLogById(Long id) {
        return auditLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit log not found"));
    }
}
