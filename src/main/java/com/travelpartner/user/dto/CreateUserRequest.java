package com.travelpartner.user.dto;

import com.travelpartner.user.enums.Platform;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {

    private String email;              // nullable

    private String username;              // nullable

    private String mobileNumber;       // required

    private String password;           // required

    private Platform platform;         // required

    private Long basicInformationId;

    private Long licenseId;

    private Long vehicleId;

    private String rememberToken;

    private String deviceToken;

    private Boolean isOtpVerified = false;

    private Long roleId;               // single role
}
