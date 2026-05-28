package com.travelpartner.rideplan.dto;

import com.travelpartner.rideplan.enums.RideStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RideStatusUpdateDto {
    private Long driverId;
    private RideStatus rideStatus;
}
