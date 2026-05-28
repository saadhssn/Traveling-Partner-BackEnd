package com.travelpartner.color.model;

import com.travelpartner.color.enums.ColorStatus;
import com.travelpartner.common.base.BaseEntity;
import com.travelpartner.vehicle.model.Vehicle;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "colors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Color extends BaseEntity {

    private String name;

    private String image;

    @Enumerated(EnumType.STRING)
    private ColorStatus status;

    @OneToMany(mappedBy = "color", cascade = CascadeType.ALL)
    private Set<Vehicle> vehicles = new HashSet<>();
}