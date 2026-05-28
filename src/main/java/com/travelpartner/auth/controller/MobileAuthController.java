package com.travelpartner.auth.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.auth.dto.AppLoginRequest;
import com.travelpartner.auth.dto.SetPasswordRequest;
import com.travelpartner.auth.dto.VerifyOtpRequest;
import com.travelpartner.auth.dto.VerifyOtpResponse;
import com.travelpartner.auth.service.MobileAuthService;
import com.travelpartner.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class MobileAuthController {

    private final MobileAuthService mobileAuthService;

    @PostMapping("/app/verify-otp")
    @AuditAction(
            action = "VERIFY_OTP",
            module = "AUTH",
            description = "Mobile app user verified OTP"
    )
    public ResponseEntity<ApiResponse<VerifyOtpResponse>> verifyOtp(@RequestBody VerifyOtpRequest request) {

        VerifyOtpResponse response = mobileAuthService.verifyOtp(request);

        return ResponseEntity.ok(
                ApiResponse.success("OTP verified successfully", response)
        );
    }

    @PostMapping("/app/set-password")
    @AuditAction(
            action = "SET_PASSWORD",
            module = "AUTH",
            description = "Mobile user set account password"
    )
    public ResponseEntity<?> setPassword(@RequestBody SetPasswordRequest request) {
        try {
            String response = mobileAuthService.setPassword(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/app/login")
    @AuditAction(
            action = "LOGIN",
            module = "AUTH",
            description = "Mobile app user logged in"
    )
    public ResponseEntity<?> login(@RequestBody AppLoginRequest request) {
        try {
            Map<String, Object> response = mobileAuthService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

}