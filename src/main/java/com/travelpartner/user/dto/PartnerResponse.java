package com.travelpartner.user.dto;

import com.travelpartner.user.enums.UserStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerResponse {

    private Long id;
    private String name;
    private String email;
    private String mobileNumber;
    private String cnicNumber;
    private String cnicFront;
    private String cnicBack;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserStatus status;
    private Set<String> roles;

    // ONLY BASIC INFO
    private String city;
    private String profilePicture;
}