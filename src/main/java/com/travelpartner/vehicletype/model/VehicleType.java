package com.travelpartner.vehicletype.model;

import com.travelpartner.brand.model.Brand;
import com.travelpartner.common.base.BaseEntity;
import com.travelpartner.vehicletype.enums.VehicleTypeStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vehicle_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleType extends BaseEntity {

    private String name;

    private String image;

    @Enumerated(EnumType.STRING)
    private VehicleTypeStatus status;

    @OneToMany(mappedBy = "vehicleType", cascade = CascadeType.ALL)
    private Set<Brand> brands = new HashSet<>();
}