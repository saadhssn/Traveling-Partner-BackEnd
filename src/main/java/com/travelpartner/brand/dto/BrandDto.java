package com.travelpartner.brand.dto;

import com.travelpartner.brand.enums.BrandStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandDto {

    private Long id;

    private String name;

    private String image;

    private BrandStatus status;

    private Long vehicleTypeId;

    private Boolean isDeleted;
}