package com.travelpartner.audit.controller;

import com.travelpartner.audit.dto.AuditLogResponse;
import com.travelpartner.audit.model.AuditLog;
import com.travelpartner.audit.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping("/getAll")
    public Page<AuditLogResponse> getAllLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return auditLogService.getAllLogs(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
    }

    @GetMapping("/getById/{id}")
    public AuditLog getById(@PathVariable Long id) {
        return auditLogService.getLogById(id);
    }
}
