package com.travelpartner.user.dto;

import com.travelpartner.basicinformation.dto.BasicInformationDto;
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
public class PartnerFullResponse {

    private Long id;
    private String email;
    private String username;
    private String mobileNumber;
    private UserStatus status;
    private Platform platform;
    private Set<String> roles;
    private String referralCode;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ONLY THIS
    private BasicInformationDto basicInformation;
}
