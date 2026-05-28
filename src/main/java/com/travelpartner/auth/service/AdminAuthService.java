package com.travelpartner.auth.service;

import com.travelpartner.auth.dto.AdminLoginRequest;
import com.travelpartner.auth.dto.AdminLoginResponse;

public interface AdminAuthService {
    AdminLoginResponse login(AdminLoginRequest request);
}
