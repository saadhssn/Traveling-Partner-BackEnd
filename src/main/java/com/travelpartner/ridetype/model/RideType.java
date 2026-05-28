package com.travelpartner.ridetype.model;

import com.travelpartner.common.base.BaseEntity;
import com.travelpartner.ridetype.enums.RideModule;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ride_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideType extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RideModule module;
}