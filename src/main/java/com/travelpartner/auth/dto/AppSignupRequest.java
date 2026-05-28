package com.travelpartner.auth.dto;

import lombok.Data;

@Data
public class AppSignupRequest {
    private String email;             // nullable
    private String username;             // nullable
    private String mobileNumber;      // required
    private String password;          // nullable initially
    private Long basicInformationId;  // nullable
    private Long licenseId;           // nullable
    private Long vehicleId;           // nullable
    private String rememberToken;     // nullable
    private String deviceToken;       // nullable
    private Long roleId;              // nullable initially
    private String referralCode;
}
