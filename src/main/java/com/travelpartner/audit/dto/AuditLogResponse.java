package com.travelpartner.audit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AuditLogResponse {

    private Long id;
    private Long userId;
    private String userType;
    private String action;
    private String module;
    private String description;
    private String ipAddress;
    private LocalDateTime createdAt;

    private String mobileNumber;
}