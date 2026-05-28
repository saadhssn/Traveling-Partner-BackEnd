package com.travelpartner.vehicletype.dto;

import com.travelpartner.vehicletype.enums.VehicleTypeStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleTypeDto {

    private Long id;

    private String name;

    private String image;

    private VehicleTypeStatus status;
}
