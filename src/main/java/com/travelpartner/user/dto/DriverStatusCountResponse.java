package com.travelpartner.user.dto;

import com.travelpartner.user.enums.UserStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverStatusCountResponse {

    private long active;
    private long inactive;
    private long blocked;
    private long pending;
    private long approved;
}