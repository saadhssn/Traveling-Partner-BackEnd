package com.travelpartner.user.dto;

import com.travelpartner.user.enums.UserStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerFilterRequest {
    private String search;
    private String city;
    private UserStatus status;
    private String role; // we will force PARTNER
}
