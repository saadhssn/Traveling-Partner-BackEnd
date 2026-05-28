package com.travelpartner.user.dto;

import com.travelpartner.user.enums.DocumentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverDocumentTypeResponse {

    private Long id;
    private String email;
    private String name;
    private String mobileNumber;
    private String documentType;
    private DocumentStatus status;
    private LocalDateTime createdAt;
}
