package com.travelpartner.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class AuthResponse {

    private Long userId;
    private String accessToken;
    private String email;
    private String mobileNumber;
    private Set<String> roles;
}
