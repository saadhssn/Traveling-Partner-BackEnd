package com.travelpartner.auth.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.auth.service.RoleAssignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RoleAssignController {

    private final RoleAssignService roleAssignService;

    @PostMapping("/user/roleassign")
    @AuditAction(
            action = "ASSIGN_ROLE",
            module = "USER",
            description = "Admin assigned role to user"
    )
    public ResponseEntity<ApiResponse<String>> assignRole(@RequestBody Map<String, Object> requestData) {
        try {
            String role = (String) requestData.get("role");
            Long userId = ((Number) requestData.get("userId")).longValue();

            roleAssignService.assignRoleToUser(role, userId);

            return ResponseEntity.ok(ApiResponse.success("Role assigned successfully.", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400)
                    .body(ApiResponse.<String>error(400, e.getMessage())); // <-- pass int statusCode
        }
    }
}