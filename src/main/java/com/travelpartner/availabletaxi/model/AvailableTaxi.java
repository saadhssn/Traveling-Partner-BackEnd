package com.travelpartner.availabletaxi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.travelpartner.availabletaxi.enums.DriverStatus;
import com.travelpartner.availabletaxi.enums.Gender;
import com.travelpartner.common.base.BaseEntity;
import com.travelpartner.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "available_taxis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableTaxi extends BaseEntity {

    // Only DRIVER role allowed
    @ManyToOne
    @JoinColumn(name = "driver_id")
    @JsonBackReference
    private User driver;

    private String city;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private DriverStatus driverStatus;

    private Double latitude;

    private Double longitude;

    @Column(name = "online_since")
    private LocalDateTime onlineSince;

    @Column(name = "confirmation_sent_at")
    private LocalDateTime confirmationSentAt;

    @Column(name = "awaiting_confirmation")
    private Boolean awaitingConfirmation = false;
}