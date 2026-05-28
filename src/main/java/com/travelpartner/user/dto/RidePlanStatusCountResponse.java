package com.travelpartner.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RidePlanStatusCountResponse {

    private long requested;
    private long accepted;
    private long started;
    private long completed;
    private long canceled;
}