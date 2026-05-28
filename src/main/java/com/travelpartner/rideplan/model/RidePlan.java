package com.travelpartner.rideplan.model;

import com.travelpartner.common.base.BaseEntity;
import com.travelpartner.rideplan.enums.RideStatus;
import com.travelpartner.ridetype.model.RideType;
import com.travelpartner.role.model.Role;
import com.travelpartner.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "ride_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RidePlan extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private User driver;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "ride_type_id")
    private RideType rideType;

    private LocalDate date;
    private String address;
    private String city;

    private Integer stuff;
    private Integer bagsCount;

    private String pickUpLocation;
    private String dropOffLocation;

    // NEW
    private Double pickupLatitude;
    private Double pickupLongitude;

    private Double dropoffLatitude;
    private Double dropoffLongitude;

    // NEW
    private String estimatedDistance;

    private String estimatedDuration;

    private Integer estimatedDurationInSeconds;

    private Integer tourDays;

    private Boolean meal;

    private Integer seats;

    private Integer seatsAvailable;

    private Integer seatsReserved;

    private Integer visitingPoints;

    private Boolean pets;

    private Boolean smoke;

    private Boolean ac;

    private String fare;

    private String driverQuotedFare;

    private String totalPassenger;

    private String partnerQuotedFare;

    @Enumerated(EnumType.STRING)
    private RideStatus rideStatus;

    private Boolean female;

    private LocalTime time;

    private String note;
}