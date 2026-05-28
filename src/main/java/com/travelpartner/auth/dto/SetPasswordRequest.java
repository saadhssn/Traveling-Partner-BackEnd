package com.travelpartner.auth.dto;

import lombok.Data;

@Data
public class SetPasswordRequest {

    private String mobileNumber;
    private String password;
    private String confirmPassword;
}
