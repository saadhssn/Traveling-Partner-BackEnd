package com.travelpartner.auth.service;

import com.travelpartner.auth.dto.*;
import com.travelpartner.user.dto.UserResponse;

public interface AuthService {

    UserResponse adminSignup(AdminSignupRequest request);

    UserResponse appSignup(AppSignupRequest request);

}
