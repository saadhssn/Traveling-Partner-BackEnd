package com.travelpartner.basicinformation.model;

import com.travelpartner.common.base.BaseEntity;
import com.travelpartner.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "basic_information")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasicInformation extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String firstName;

    private String lastName;

    private String gender;

    private String whatsApp;

    @Column(unique = true)
    private String email;

    @Pattern(regexp = "\\d{13}", message = "CNIC must be 13 digits")
    @Column(name = "cnic_number", length = 13, unique = true)
    private String cnicNumber;

    private String cnicFront;
    private String cnicBack;
    private String profilePicture;
    private String referralCode;
    private String city;

    private Boolean acceptTerm = false;

    // Soft delete
    private Boolean isDeleted = false;
    private LocalDateTime deletedAt;
}
