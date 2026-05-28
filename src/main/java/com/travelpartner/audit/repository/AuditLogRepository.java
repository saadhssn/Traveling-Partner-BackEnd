package com.travelpartner.audit.repository;

import com.travelpartner.audit.dto.AuditLogResponse;
import com.travelpartner.audit.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    @Query("""
    SELECT new com.travelpartner.audit.dto.AuditLogResponse(
        a.id,
        a.userId,
        a.userType,
        a.action,
        a.module,
        a.description,
        a.ipAddress,
        a.createdAt,
        u.mobileNumber
    )
    FROM AuditLog a
    LEFT JOIN com.travelpartner.user.model.User u ON a.userId = u.id
""")
    Page<AuditLogResponse> findAllWithUser(Pageable pageable);
}
