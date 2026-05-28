package com.travelpartner.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminLoginResponse {

    private Long id;

    private String name;

    private String email;

    private String mobileNumber;

    private String role;

    private String token;
}