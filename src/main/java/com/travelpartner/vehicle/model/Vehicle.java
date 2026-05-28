package com.travelpartner.vehicle.model;

import com.travelpartner.brand.model.Brand;
import com.travelpartner.color.model.Color;
import com.travelpartner.common.base.BaseEntity;
import com.travelpartner.modelnumber.model.ModelNumber;
import com.travelpartner.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "model_number_id", nullable = false)
    private ModelNumber modelNumber;

    @ManyToOne
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;

    private String registrationNo;
    private String registrationFront;
    private String registrationBack;

    private String outdoorImages;
    private String indoorImages;

    private boolean ac;
    private boolean petsAllowed;
    private boolean smokingAllowed;

    private Boolean vehicleVerified = false;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}