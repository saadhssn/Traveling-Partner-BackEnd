package com.travelpartner.health;

import com.travelpartner.audit.annotation.AuditAction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    @AuditAction(action = "READ", module = "HEALTH", description = "Check service health")
    public String health() {
        return "OK";
    }
}