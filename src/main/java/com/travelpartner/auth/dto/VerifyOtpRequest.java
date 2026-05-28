package com.travelpartner.auth.dto;

import lombok.Data;

@Data
public class VerifyOtpRequest {
    private String mobileNumber;
    private String otp;
}
