package com.travelpartner.availabletaxi.dto;

import com.travelpartner.availabletaxi.enums.DriverStatus;
import com.travelpartner.availabletaxi.enums.Gender;
import com.travelpartner.license.dto.LicenseDto;
import com.travelpartner.vehicle.dto.VehicleDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableTaxiFullDto {

    private Long id;
    private Long driverId;
    private String driverName;
    private String city;
    private Gender gender;
    private DriverStatus driverStatus;
    private Double latitude;
    private Double longitude;

    private VehicleDto vehicle; // can be null
    private LicenseDto license; // can be null
}