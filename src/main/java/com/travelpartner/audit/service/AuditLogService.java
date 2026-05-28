package com.travelpartner.audit.service;

import com.travelpartner.audit.dto.AuditLogResponse;
import com.travelpartner.audit.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditLogService {

    void saveAuditLog(AuditLog log);

    Page<AuditLogResponse> getAllLogs(Pageable pageable);

    AuditLog getLogById(Long id);

}
