package com.travelpartner.user.dto;

import com.travelpartner.user.enums.UserStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleAgentFilterRequest {

    private String search;
    private UserStatus status;
    private Boolean includeDeleted;
}