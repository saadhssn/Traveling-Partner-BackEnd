package com.travelpartner.file.dto;

import com.travelpartner.user.enums.DocumentStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentStatusUpdateDto {

    private DocumentStatus cnicStatus;          // APPROVED, REJECTED, or null
    private DocumentStatus licenseStatus;       // APPROVED, REJECTED, or null
    private DocumentStatus vehicleDocStatus;    // APPROVED, REJECTED, or null
    private String reason;                      // optional, used for rejection
}