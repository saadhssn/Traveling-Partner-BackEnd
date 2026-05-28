package com.travelpartner.auth.dto;

import lombok.Data;

@Data
public class AdminLoginRequest {

    private String email;
    private String mobileNumber;
    private String password;
    private String otp;
}
