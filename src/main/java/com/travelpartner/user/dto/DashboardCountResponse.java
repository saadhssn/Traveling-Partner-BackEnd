package com.travelpartner.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardCountResponse {

    private long totalDrivers;
    private long totalPartners;
    private long totalSalesAgents;
    private long totalRidePlans;
}
