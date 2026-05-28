package com.travelpartner.auth.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.auth.dto.AdminSignupRequest;
import com.travelpartner.auth.dto.AppSignupRequest;
import com.travelpartner.auth.service.AuthService;
import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /* ================= ADMIN SIGNUP ================= */

    @PostMapping("/admin/signup")
    @AuditAction(
            action = "SIGNUP",
            module = "USER",
            description = "Portal user signup"
    )
    public ResponseEntity<ApiResponse<UserResponse>> adminSignup(
            @RequestBody AdminSignupRequest request) {

        UserResponse response = authService.adminSignup(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Admin registered successfully", response));
    }

    /* ================= APP SIGNUP ================= */

    @PostMapping("/app/signup")
    @AuditAction(
            action = "SIGNUP",
            module = "USER",
            description = "Mobile app user signup"
    )
    public ResponseEntity<ApiResponse<UserResponse>> appSignup(
            @RequestBody AppSignupRequest request) {

        UserResponse response = authService.appSignup(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "User registered successfully (OTP verification pending)",
                        response
                ));
    }
}
