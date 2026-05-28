package com.travelpartner.ridetype.dto;

import com.travelpartner.ridetype.enums.RideModule;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideTypeDto {

    private Long id;

    @NotBlank(message = "Ride type name is required")
    private String name;

    private RideModule module;
}