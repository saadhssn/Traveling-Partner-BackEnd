package com.travelpartner.contactus.model;

import com.travelpartner.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contact_us")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactUs extends BaseEntity {

    @Column(nullable = true)
    private String name;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(nullable = true)
    private String phoneNumber;

    @Column(nullable = true)
    private String photoPath;
}