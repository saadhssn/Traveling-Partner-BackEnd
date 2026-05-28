package com.travelpartner.newsletter.model;

import com.travelpartner.common.base.BaseEntity;
import com.travelpartner.newsletter.enums.NewsletterStatus;
import com.travelpartner.role.model.Role;
import com.travelpartner.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "newsletters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Newsletter extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String message;

    private String attachedFile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /*
        Optional:
        If admin wants to send newsletter to role users
        like DRIVER / PARTNER / ADMIN
    */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Enumerated(EnumType.STRING)
    private NewsletterStatus status;

    @Column(name = "user_name")
    private String userName;

    private Boolean isDeleted = false;

    private LocalDateTime deletedAt;

}