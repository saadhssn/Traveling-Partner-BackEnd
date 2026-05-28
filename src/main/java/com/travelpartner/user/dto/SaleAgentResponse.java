package com.travelpartner.user.dto;

import com.travelpartner.user.enums.UserStatus;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleAgentResponse {

    private Long id;
    private String email;
    private String username;
    private String mobileNumber;
    private String name;
    private UserStatus status;
    private String cnicNumber;
    private String cnicFront;
    private String cnicBack;
    private Set<String> roles;
}