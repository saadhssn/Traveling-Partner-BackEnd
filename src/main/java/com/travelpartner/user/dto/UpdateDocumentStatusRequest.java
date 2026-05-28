package com.travelpartner.user.dto;

import com.travelpartner.user.enums.DocumentStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateDocumentStatusRequest {

    private DocumentStatus cnicStatus;
    private DocumentStatus licenseStatus;
    private DocumentStatus vehicleStatus;

    private String rejectionReason; // optional
}