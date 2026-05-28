package com.travelpartner.auth.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.auth.dto.AdminLoginRequest;
import com.travelpartner.auth.dto.AdminLoginResponse;
import com.travelpartner.auth.dto.OtpRequest;
import com.travelpartner.auth.service.AdminAuthService;
import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.user.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminAuthService;
    private final OtpService otpService;

    @PostMapping("/login")
    @AuditAction(
            action = "LOGIN",
            module = "AUTH",
            description = "Admin logged into portal"
    )
    public ResponseEntity<ApiResponse<AdminLoginResponse>> login(@RequestBody AdminLoginRequest request) {
        try {
            AdminLoginResponse response = adminAuthService.login(request);
            return ResponseEntity.ok(ApiResponse.success(200, "Admin login successful", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error(401, e.getMessage()));
        }
    }

    @PostMapping("/generate/otp")
    @AuditAction(
            action = "GENERATE OTP",
            module = "AUTH",
            description = "Generate OTP for portal"
    )
    public ResponseEntity<ApiResponse<String>> generateOtp(@RequestBody OtpRequest request) {

        String otp = otpService.createOrGenerateOtp(request.getMobileNumber());

        return ResponseEntity.ok(
                ApiResponse.success(200, "OTP generated successfully", otp)
        );
    }
}