package com.travelpartner.brand.model;

import com.travelpartner.brand.enums.BrandStatus;
import com.travelpartner.common.base.BaseEntity;
import com.travelpartner.vehicle.model.Vehicle;
import com.travelpartner.vehicletype.model.VehicleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Brand extends BaseEntity {

    private String name;

    private String image;

    @Enumerated(EnumType.STRING)
    private BrandStatus status;

    @ManyToOne
    @JoinColumn(name = "vehicle_type_id")
    private VehicleType vehicleType;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
    private Set<Vehicle> vehicles = new HashSet<>();
}