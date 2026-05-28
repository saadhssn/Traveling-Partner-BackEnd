package com.travelpartner.auth.dto;

import lombok.Data;

@Data
public class AppLoginRequest {

    private String mobileNumber;
    private String password;
}
