package com.travelpartner.auth.dto;

import lombok.Data;

@Data
public class CreateSalesAgentRequest {

    private String email;
    private String username;
    private String mobileNumber;
    private String password;

    private String name;
    private String gender;
    private String cnicNumber;
    private String cnicFront;
    private String cnicBack;
    private String status;

}