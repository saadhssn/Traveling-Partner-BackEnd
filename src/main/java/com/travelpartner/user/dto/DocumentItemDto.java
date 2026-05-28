package com.travelpartner.user.dto;

import com.travelpartner.user.enums.DocumentStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentItemDto {

    private String documentType;

    private String side;

    private String url;

    private DocumentStatus status;
}