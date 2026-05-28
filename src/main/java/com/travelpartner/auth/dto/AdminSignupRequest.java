package com.travelpartner.auth.dto;

import lombok.Data;

@Data
public class AdminSignupRequest {

    private String email;
    private String username;
    private String mobileNumber;
    private String password;


}
