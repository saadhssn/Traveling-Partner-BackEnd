package com.travelpartner.user.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.user.dto.*;
import com.travelpartner.user.enums.UserStatus;
import com.travelpartner.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /* ================= GET ================= */

    @GetMapping("/{id}")
    @AuditAction(action = "READ", module = "USER", description = "Get user by id")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/mobile/{mobile}")
    @AuditAction(action = "READ", module = "USER", description = "Get user by mobile")
    public ResponseEntity<UserResponse> getByMobile(@PathVariable String mobile) {
        return ResponseEntity.ok(userService.getUserByMobile(mobile));
    }

    @GetMapping
    @AuditAction(action = "READ", module = "USER", description = "Get all active users")
    public ResponseEntity<List<UserResponse>> getAllActive() {
        return ResponseEntity.ok(userService.getAllActiveUsers());
    }

    @GetMapping("/all")
    @AuditAction(action = "READ", module = "USER", description = "Get all users including deleted")
    public ResponseEntity<List<UserResponse>> getAllIncludingDeleted() {
        return ResponseEntity.ok(userService.getAllUsersIncludingDeleted());
    }

    /* ================= DELETE ================= */

    @DeleteMapping("/soft-delete/{id}")
    @AuditAction(action = "DELETE", module = "USER", description = "Soft delete user")
    public ResponseEntity<String> softDelete(@PathVariable Long id) {
        userService.softDeleteUser(id);
        return ResponseEntity.ok("User soft deleted successfully");
    }

    @DeleteMapping("/hard-delete/{id}")
    @AuditAction(action = "DELETE", module = "USER", description = "Hard delete user")
    public ResponseEntity<String> hardDelete(@PathVariable Long id) {
        userService.hardDeleteUser(id);
        return ResponseEntity.ok("User hard deleted successfully");
    }

    @PutMapping("/restore/{id}")
    @AuditAction(action = "UPDATE", module = "USER", description = "Restore deleted user")
    public ResponseEntity<String> restore(@PathVariable Long id) {
        userService.restoreUser(id);
        return ResponseEntity.ok("User restored successfully");
    }

    /* ================= UPDATE ================= */

    @PutMapping("/{id}")
    @AuditAction(action = "UPDATE", module = "USER", description = "Update user")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @GetMapping("/drivers")
    @AuditAction(action = "READ", module = "USER", description = "Get all drivers")
    public ResponseEntity<?> getDrivers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        UserFilterRequest filter = UserFilterRequest.builder()
                .search(search)
                .city(city)
                .status(status != null && !status.isEmpty()
                        ? UserStatus.valueOf(status.toUpperCase())
                        : null)
                .build();

        Page<UserResponse> response = userService.getDrivers(filter, page, size);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/drivers/{id}")
    @AuditAction(action = "READ", module = "USER", description = "Get driver full details by id")
    public ResponseEntity<DriverFullResponse> getDriverFull(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getDriverFullById(id));
    }

    @PutMapping("/status/{id}")
    @AuditAction(action = "UPDATE", module = "USER", description = "Update user status")
    public ResponseEntity<String> updateUserStatus(
            @PathVariable("id") Long userId,
            @RequestBody @Valid UpdateUserStatusRequest request) {
        try {
            userService.updateUserStatus(userId, request);
            return ResponseEntity.ok("User status updated successfully");
        } catch (Exception e) {
            e.printStackTrace(); // <- prints actual exception
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/documents")
    @AuditAction(action = "READ", module = "USER", description = "Get all user documents")
    public ResponseEntity<Page<UserDocumentResponse>> getAllDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(userService.getAllDocuments(page, size));
    }

    @GetMapping("/documents/{id}")
    @AuditAction(action = "READ", module = "USER", description = "Get user documents by id")
    public ResponseEntity<UserDocumentResponse> getUserDocumentsById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserDocumentsById(id));
    }

    @PutMapping("/documents/status/{id}")
    @AuditAction(action = "UPDATE", module = "USER", description = "Update user document status")
    public ResponseEntity<String> updateDocumentStatus(
            @PathVariable Long id,
            @RequestBody UpdateDocumentStatusRequest request) {

        userService.updateDocumentStatus(id, request);
        return ResponseEntity.ok("Document status updated successfully");
    }

    @GetMapping("/sale-agents")
    @AuditAction(action = "READ", module = "USER", description = "Get all sale agents")
    public ResponseEntity<Page<SaleAgentResponse>> getSaleAgents(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        UserStatus userStatus = null;

        if (status != null && !status.trim().isEmpty()) {
            try {
                userStatus = UserStatus.valueOf(status.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest()
                        .body(Page.empty()); // or throw custom exception
            }
        }

        SaleAgentFilterRequest filter = SaleAgentFilterRequest.builder()
                .search(search)
                .status(userStatus)
                .build();

        return ResponseEntity.ok(userService.getAllSaleAgents(filter, page, size));
    }

    @GetMapping("/sale-agents/{id}")
    @AuditAction(action = "READ", module = "USER", description = "Get sale agent by id")
    public ResponseEntity<SaleAgentResponse> getSaleAgentById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getSaleAgentById(id));
    }

    @GetMapping("/partners")
    @AuditAction(action = "READ", module = "USER", description = "Get all partners")
    public ResponseEntity<Page<PartnerResponse>> getPartners(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        UserStatus userStatus = null;

        if (status != null && !status.trim().isEmpty()) {
            try {
                userStatus = UserStatus.valueOf(status.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Page.empty());
            }
        }

        UserFilterRequest filter = UserFilterRequest.builder()
                .search(search)
                .city(city)
                .status(userStatus)
                .role("PARTNER")
                .build();

        return ResponseEntity.ok(userService.getPartners(filter, page, size));
    }

    @GetMapping("/partners/{id}")
    @AuditAction(action = "READ", module = "USER", description = "Get partner full details by id")
    public ResponseEntity<PartnerFullResponse> getPartnerFull(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getPartnerFullById(id));
    }

    @GetMapping("/counts")
    @AuditAction(action = "READ", module = "DASHBOARD", description = "Get dashboard counts")
    public DashboardCountResponse getCounts() {
        return userService.getDashboardCounts();
    }

    @GetMapping("/driver-status-counts")
    @AuditAction(action = "READ", module = "DASHBOARD", description = "Get driver status counts")
    public DriverStatusCountResponse getDriverStatusCounts() {
        return userService.getDriverStatusCounts();
    }

    @GetMapping("/ride-status-count")
    @AuditAction(action = "READ", module = "DASHBOARD", description = "Get ride plan status counts")
    public RidePlanStatusCountResponse getStatusCounts() {
        return userService.getRidePlanStatusCounts();
    }

    @GetMapping("/graph/last-14-days")
    @AuditAction(action = "READ", module = "DASHBOARD", description = "Get last 14 days ride plan graph")
    public RidePlanGraphResponse getLast14DaysGraph() {
        return userService.getRidePlanLast14DaysGraph();
    }

    @GetMapping("/drivers/document-types")
    @AuditAction(action = "READ", module = "USER", description = "Get driver document types")
    public ResponseEntity<Page<DriverDocumentTypeResponse>> getDriverDocumentTypes(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        UserFilterRequest filter = UserFilterRequest.builder()
                .search(search)
                .city(city)
                .status(status != null && !status.isEmpty()
                        ? UserStatus.valueOf(status.toUpperCase())
                        : null)
                .build();

        return ResponseEntity.ok(
                userService.getDriverDocumentTypes(filter, page, size)
        );
    }

    @PutMapping("/device-token/{userId}")
    public ApiResponse<String> updateDeviceToken(
            @PathVariable Long userId,
            @RequestBody DeviceTokenRequest request
    ) {

        return userService.updateDeviceToken(
                userId,
                request.getDeviceToken()
        );
    }
}