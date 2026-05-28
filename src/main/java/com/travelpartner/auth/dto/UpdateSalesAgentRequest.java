package com.travelpartner.auth.dto;

import lombok.Data;

@Data
public class UpdateSalesAgentRequest {

    private String email;
    private String username;
    private String mobileNumber;

    private String name;
    private String gender;
    private String cnicNumber;
    private String cnicFront;
    private String cnicBack;

    private String password; // optional (only if updating)
    private String status;
}