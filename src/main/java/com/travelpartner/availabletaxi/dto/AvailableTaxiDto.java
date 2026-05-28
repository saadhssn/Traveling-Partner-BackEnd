package com.travelpartner.availabletaxi.dto;

import com.travelpartner.availabletaxi.enums.DriverStatus;
import com.travelpartner.availabletaxi.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableTaxiDto {

    private Long id;

    private Long driverId;

    private String city;

    private Gender gender;

    private DriverStatus driverStatus;

    private Double latitude;

    private Double longitude;
}