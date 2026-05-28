package com.travelpartner.auth.service;

import com.travelpartner.auth.dto.AppLoginRequest;
import com.travelpartner.auth.dto.SetPasswordRequest;
import com.travelpartner.auth.dto.VerifyOtpRequest;
import com.travelpartner.auth.dto.VerifyOtpResponse;

import java.util.Map;

public interface MobileAuthService {
    VerifyOtpResponse verifyOtp(VerifyOtpRequest request);

    String setPassword(SetPasswordRequest request);

    Map<String, Object> login(AppLoginRequest request);
}
