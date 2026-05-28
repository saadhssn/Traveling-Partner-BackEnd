package com.travelpartner.role.repository;

import com.travelpartner.role.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    boolean existsByName(String name);
    boolean existsBySlug(String slug);

    Optional<Role> findBySlug(String slug);
    Optional<Role> findByName(String name);
}