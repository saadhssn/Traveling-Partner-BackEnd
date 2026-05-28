package com.travelpartner.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

    private String email;
    private String username;
    private String cnicNumber;
    private String mobileNumber;
    private String name;
    private String gender;
    private String deviceToken;
}
