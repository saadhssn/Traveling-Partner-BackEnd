package com.travelpartner.basicinformation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasicInformationDto {

    private Long userId;

    private String firstName;

    private String lastName;

    private String gender;
    private String whatsApp;

    private String email;

    @Pattern(regexp = "\\d{13}", message = "CNIC must be 13 digits")
    private String cnicNumber;

    private String cnicFront;
    private String cnicBack;
    private String profilePicture;
    private String referralCode;
    private Boolean acceptTerm;
    private String city;

    // For filtering in GET /getAll
    private Boolean filterDeleted;
}
