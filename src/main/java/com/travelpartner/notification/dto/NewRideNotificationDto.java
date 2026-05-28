package com.travelpartner.notification.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewRideNotificationDto {

    private Long rideId;
    private String pickup;
    private String drop;
    private String city;
    private String fare;
}