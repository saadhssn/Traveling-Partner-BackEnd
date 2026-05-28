package com.travelpartner.user.dto;

import com.travelpartner.user.enums.Platform;
import com.travelpartner.user.enums.UserStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFilterRequest {

    private String search;        // name/email/mobile
    private String role;          // DRIVER / PARTNER
    private Platform platform;    // ANDROID / IOS / WEB
    private UserStatus status;    // ACTIVE / PENDING
    private String city;
    private Boolean includeDeleted = false;
}
