package com.travelpartner.license.repository;

import com.travelpartner.license.model.License;
import com.travelpartner.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LicenseRepository extends JpaRepository<License, Long> {

    @Query("""
        SELECT l FROM License l 
        WHERE l.isDeleted = false
        AND (:verified IS NULL OR l.licenseVerified = :verified)
    """)
    Page<License> findByIsDeletedFalseAndVerified(
            @Param("verified") Boolean verified,
            Pageable pageable);

    Page<License> findByIsDeletedFalse(Pageable pageable);

    Optional<License> findByUser(User user);

    Optional<License> findByLicenseNoAndIdNot(String licenseNo, Long id);

    boolean existsByLicenseNo(String licenseNo);
}
