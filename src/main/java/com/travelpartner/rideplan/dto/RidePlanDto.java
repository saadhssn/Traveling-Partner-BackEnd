package com.travelpartner.rideplan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.travelpartner.rideplan.enums.RideStatus;
import com.travelpartner.vehicle.dto.VehicleDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RidePlanDto {

    private Long id;

    private Long userId;

    private Long driverId;

    private String userName;

    private String driverName;

    private String role;

    private String rideType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String address;

    private String city;

    private Integer stuff;

    private Integer bagsCount;

    private String pickUpLocation;

    private String dropOffLocation;

    private Integer tourDays;

    private Boolean meal;

    private Integer seats;

    private Integer seatsAvailable;

    private Integer seatsReserved;

    private Integer visitingPoints;

    private Boolean pets;

    private Boolean smoke;

    private Boolean ac;

    private Double pickupLatitude;

    private Double pickupLongitude;

    private Double dropoffLatitude;

    private Double dropoffLongitude;

    private String estimatedDistance;

    private String estimatedDuration;

    private Integer estimatedDurationInSeconds;

    private String fare;

    private String driverQuotedFare;

    private String totalPassenger;

    private String partnerQuotedFare;

    private RideStatus rideStatus;

    private Boolean female;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime time;

    private String note;

    private VehicleDto vehicle;
}