package com.travelpartner.basicinformation.repository;

import com.travelpartner.basicinformation.model.BasicInformation;
import com.travelpartner.rideplan.model.RidePlan;
import com.travelpartner.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BasicInformationRepository extends JpaRepository<BasicInformation, Long> {

    @Query("""
        SELECT b FROM BasicInformation b
        WHERE (:deleted IS NULL OR b.isDeleted = :deleted)
    """)
    Page<BasicInformation> findByIsDeleted(@Param("deleted") Boolean deleted, Pageable pageable);

    Optional<BasicInformation> findByUser(User user);

    boolean existsByEmail(String email);
    boolean existsByWhatsApp(String whatsApp);
    boolean existsByCnicNumber(String cnicNumber);

    // For update
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByWhatsAppAndIdNot(String whatsApp, Long id);
    boolean existsByCnicNumberAndIdNot(String cnicNumber, Long id);

    Optional<BasicInformation> findByUser_Id(Long userId);
}
