package com.travelpartner.license.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenseDto {

    private Long userId;

    private String licenseNo;

    private String licenseFront;
    private String licenseBack;

    private Boolean licenseVerified;

    // Used only for filtering in GET /getAll
    private Boolean filterVerified;
}
