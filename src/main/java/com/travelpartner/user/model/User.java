package com.travelpartner.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.travelpartner.availabletaxi.model.AvailableTaxi;
import com.travelpartner.basicinformation.model.BasicInformation;
import com.travelpartner.common.base.BaseEntity;
import com.travelpartner.role.model.Role;
import com.travelpartner.user.enums.DocumentStatus;
import com.travelpartner.user.enums.Platform;
import com.travelpartner.user.enums.UserStatus;
import com.travelpartner.license.model.License;
import com.travelpartner.vehicle.model.Vehicle;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    /* ================= BASIC AUTH ================= */

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    @Column(name = "mobile_number", unique = true)
    private String mobileNumber;

    @Column(nullable = true)
    private String password;

    private String otp;

    /* ================= STATUS & PLATFORM ================= */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    /* ================= ROLES ================= */

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnoreProperties("users")
    private Set<Role> roles = new HashSet<>(); // keep default

    /* ================= DEFAULT CALLBACK ================= */

    @PrePersist
    @PreUpdate
    private void ensureDefaults() {
        if (roles == null) {
            roles = new HashSet<>();
        }
        if (isDeleted == null) {
            isDeleted = false;
        }
        if (isOtpVerified == null) {
            isOtpVerified = false;
        }
    }

    /* ================= PROFILE INFO ================= */

    @Column(name = "cnic_number", unique = true)
    private String cnicNumber;

    @Column(name = "cnic_front")
    private String cnicFront;

    @Column(name = "cnic_back")
    private String cnicBack;

    private String name;
    private String gender;

    /* ================= MODULE REFERENCES (LOOSE COUPLING) ================= */

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private BasicInformation basicInformation;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private License license;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Vehicle vehicle;

    @OneToMany(mappedBy = "driver")
    @JsonManagedReference
    private List<AvailableTaxi> availableTaxis;

    /* ================= FLAGS ================= */

    @Column(name = "remember_token")
    private String rememberToken;

    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "is_otp_verified")
    private Boolean isOtpVerified = false;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    private Boolean licenseVerified = false;
    private Boolean vehicleVerified = false;

    /* ================= REFERRAL SYSTEM ================= */

    @Column(name = "referral_code", unique = true)
    private String referralCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referred_by_agent_id")
    private User referredByAgent;

    @Enumerated(EnumType.STRING)
    @Column(name = "cnic_status")
    private DocumentStatus cnicStatus = DocumentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "license_status")
    private DocumentStatus licenseStatus = DocumentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_doc_status")
    private DocumentStatus vehicleDocStatus = DocumentStatus.PENDING;

    @Column(name = "document_rejection_reason")
    private String documentRejectionReason;
}
