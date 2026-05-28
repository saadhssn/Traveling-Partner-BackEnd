package com.travelpartner.user.dto;

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
public class UserResponse {

    private Long id;
    private String email;
    private String name;
    private String username;
    private String mobileNumber;
    private UserStatus status;
    private Platform platform;
    private Set<String> roles;
    private String otp;
    private String token;
    private String referralCode;
    private String city;
    private String gender;
    private String cnicNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
