package com.travelpartner.availabletaxi.dto;

import com.travelpartner.availabletaxi.enums.DriverStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableTaxiStatusDto {

    @NotNull(message = "Driver status is required")
    private DriverStatus driverStatus;
}