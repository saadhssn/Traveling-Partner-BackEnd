package com.travelpartner.user.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RidePlanGraphResponse {

    private List<String> dates;   // X-axis
    private List<Long> counts;    // Y-axis
}