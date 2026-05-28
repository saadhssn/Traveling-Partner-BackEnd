package com.travelpartner.vehicle.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDto {

    private Long id;

    private Long modelNumberId;
    private String modelNumberName;

    private Long colorId;
    private String colorName;

    private String registrationNo;
    private String registrationFront;
    private String registrationBack;

    private String outdoorImages;
    private String indoorImages;

    private Boolean ac;
    private boolean petsAllowed;
    private boolean smokingAllowed;

    private Boolean vehicleVerified;

    private Long brandId;
    private String brandName;

    private Long userId;
}