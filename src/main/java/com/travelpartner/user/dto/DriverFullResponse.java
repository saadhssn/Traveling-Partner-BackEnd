package com.travelpartner.user.dto;

import com.travelpartner.basicinformation.dto.BasicInformationDto;
import com.travelpartner.license.dto.LicenseDto;
import com.travelpartner.vehicle.dto.VehicleDto;
import com.travelpartner.user.enums.Platform;
import com.travelpartner.user.enums.UserStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverFullResponse {

    private Long id;
    private String email;
    private String username;
    private String mobileNumber;
    private UserStatus status;
    private Platform platform;
    private Set<String> roles;
    private String otp;
    private String token;
    private String referralCode;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private BasicInformationDto basicInformation; // can be null
    private LicenseDto license;                     // can be null
    private VehicleDto vehicle;                     // can be null
}