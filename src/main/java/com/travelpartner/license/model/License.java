package com.travelpartner.license.model;

import com.travelpartner.common.base.BaseEntity;
import com.travelpartner.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "licenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class License extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @NotBlank(message = "License number is required")
    @Column(name = "license_no", unique = true)
    private String licenseNo;

    private String licenseFront;
    private String licenseBack;

    private Boolean licenseVerified = false;

    // Soft delete
    private Boolean isDeleted = false;
    private LocalDateTime deletedAt;
}
