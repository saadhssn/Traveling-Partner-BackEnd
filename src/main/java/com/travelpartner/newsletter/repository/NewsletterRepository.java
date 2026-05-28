package com.travelpartner.newsletter.repository;

import com.travelpartner.newsletter.enums.NewsletterStatus;
import com.travelpartner.newsletter.model.Newsletter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NewsletterRepository extends JpaRepository<Newsletter, Long> {

    Optional<Newsletter> findByIdAndIsDeletedFalse(Long id);

    @Query("""
        SELECT n FROM Newsletter n
        WHERE n.isDeleted = false
        AND (
            CAST(:search AS text) IS NULL
            OR n.message ILIKE CONCAT('%', CAST(:search AS text), '%')
            OR n.user.name ILIKE CONCAT('%', CAST(:search AS text), '%')
            OR n.role.name ILIKE CONCAT('%', CAST(:search AS text), '%')
        )
        AND (:status IS NULL OR n.status = :status)
        ORDER BY n.createdAt DESC
    """)
    Page<Newsletter> filterNewsletters(
            @Param("search") String search,
            @Param("status") NewsletterStatus status,
            Pageable pageable
    );

}