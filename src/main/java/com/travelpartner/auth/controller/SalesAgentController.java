package com.travelpartner.auth.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.auth.dto.CreateSalesAgentRequest;
import com.travelpartner.auth.dto.UpdateSalesAgentRequest;
import com.travelpartner.auth.service.SalesAgentService;
import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/sales-agent")
@RequiredArgsConstructor
public class SalesAgentController {

    private final SalesAgentService salesAgentService;

    @PostMapping("/create")
    @AuditAction(
            action = "CREATE",
            module = "SALES_AGENT",
            description = "Admin created sales agent"
    )
    public ResponseEntity<ApiResponse<UserResponse>> createSalesAgent(
            @RequestBody CreateSalesAgentRequest request) {

        try {
            UserResponse response = salesAgentService.createSalesAgent(request);
            return ResponseEntity.ok(
                    ApiResponse.success(200, "Sales agent created successfully", response)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @GetMapping("/getAll")
    @AuditAction(
            action = "VIEW",
            module = "SALES_AGENT",
            description = "Admin viewed all sales agents"
    )
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllSalesAgents() {

        try {
            List<UserResponse> response = salesAgentService.getAllSalesAgents();
            return ResponseEntity.ok(
                    ApiResponse.success(200, "Sales agents fetched successfully", response)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    @AuditAction(
            action = "UPDATE",
            module = "SALES_AGENT",
            description = "Admin updated sales agent"
    )
    public ResponseEntity<ApiResponse<UserResponse>> updateSalesAgent(
            @PathVariable Long id,
            @RequestBody UpdateSalesAgentRequest request
    ) {

        try {
            UserResponse response = salesAgentService.updateSalesAgent(id, request);
            return ResponseEntity.ok(
                    ApiResponse.success(200, "Sales agent updated successfully", response)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}