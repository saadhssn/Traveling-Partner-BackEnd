package com.travelpartner.role.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.travelpartner.common.base.BaseEntity;
import com.travelpartner.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name; // ADMIN, DRIVER, PARTNER

    @Column(nullable = false, unique = true)
    private String slug;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<User> users;
}