package com.travelpartner.user.dto;

import com.travelpartner.user.enums.DocumentStatus;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDocumentResponse {

    private Long id;
    private String mobileNumber;
    private Set<String> roles;
    private String name;
    private String gender;

    // common
    private String profilePicture;
    private String cnicFront;
    private String cnicBack;

    // driver only
    private String licenseFront;
    private String licenseBack;

    private String registrationFront;
    private String registrationBack;
    private String outdoorImages;
    private String indoorImages;

    private DocumentStatus cnicStatus;
    private DocumentStatus licenseStatus;
    private DocumentStatus vehicleDocStatus;

    private List<DocumentItemDto> documents;
}