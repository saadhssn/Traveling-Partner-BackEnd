package com.travelpartner.auth.dto;

import com.travelpartner.basicinformation.dto.BasicInformationDto;
import com.travelpartner.license.dto.LicenseDto;
import com.travelpartner.user.enums.Platform;
import com.travelpartner.user.enums.UserStatus;
import com.travelpartner.user.enums.DocumentStatus;
import com.travelpartner.vehicle.dto.VehicleDto;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class VerifyOtpResponse {

    private Long id;
    private String email;
    private String username;
    private String mobileNumber;
    private UserStatus status;
    private Platform platform;

    private Set<String> roles;

    private String otp;
    private String token;

    // DRIVER JOURNEY STATUS
    private Long basicInformationId;
    private Long licenseId;
    private Long vehicleId;

    // DOCUMENT STATUS
    private DocumentStatus cnicStatus;
    private DocumentStatus licenseStatus;
    private DocumentStatus vehicleDocStatus;

    // FULL DATA (ROLE BASED)
    private BasicInformationDto basicInformation;
    private LicenseDto license;
    private VehicleDto vehicle;
}