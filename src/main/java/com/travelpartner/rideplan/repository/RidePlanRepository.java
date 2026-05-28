package com.travelpartner.rideplan.repository;

import com.travelpartner.rideplan.enums.RideStatus;
import com.travelpartner.rideplan.model.RidePlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RidePlanRepository extends JpaRepository<RidePlan, Long> {

    @EntityGraph(attributePaths = {
            "user",
            "user.basicInformation",
            "driver",
            "driver.basicInformation",
            "role",
            "rideType"
    })
    Page<RidePlan> findAllBy(Pageable pageable);

    @EntityGraph(attributePaths = {
            "user",
            "user.basicInformation",
            "driver",
            "driver.basicInformation",
            "role",
            "rideType"
    })
    Optional<RidePlan> findById(Long id);

    @Query("""
       SELECT rp FROM RidePlan rp
       LEFT JOIN FETCH rp.user u
       LEFT JOIN FETCH u.basicInformation
       LEFT JOIN FETCH rp.driver d
       LEFT JOIN FETCH d.basicInformation
       WHERE rp.id = :id
       """)
    Optional<RidePlan> findByIdWithUserDriverAndBasicInfo(Long id);

    long count();

    long countByRideStatus(RideStatus rideStatus);

    @Query("""
        SELECT rp.rideStatus, COUNT(rp)
        FROM RidePlan rp
        GROUP BY rp.rideStatus
    """)
    List<Object[]> countRidePlansByStatus();

    @Query("""
    SELECT FUNCTION('DATE', rp.createdAt), COUNT(rp)
    FROM RidePlan rp
    WHERE rp.createdAt >= :fromDate
    GROUP BY FUNCTION('DATE', rp.createdAt)
    ORDER BY FUNCTION('DATE', rp.createdAt)
""")
    List<Object[]> countRidePlansLastDays(@Param("fromDate") LocalDateTime fromDate);

    @Modifying
    @Query("""
    UPDATE RidePlan rp
    SET rp.driver = :driver,
        rp.rideStatus = com.travelpartner.rideplan.enums.RideStatus.ACCEPTED
    WHERE rp.id = :rideId
    AND rp.rideStatus = com.travelpartner.rideplan.enums.RideStatus.REQUESTED
""")
    int acceptRide(@Param("rideId") Long rideId,
                   @Param("driver") com.travelpartner.user.model.User driver);

    @EntityGraph(attributePaths = {
            "user",
            "user.basicInformation",
            "driver",
            "driver.basicInformation",
            "role",
            "rideType"
    })
    Page<RidePlan> findByDriver_IdAndRideStatus(
            Long driverId,
            RideStatus rideStatus,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {
            "user",
            "user.basicInformation",
            "driver",
            "driver.basicInformation",
            "role",
            "rideType"
    })
    @Query("""
SELECT rp
FROM RidePlan rp
WHERE rp.driver.id = :driverId
AND rp.rideStatus = com.travelpartner.rideplan.enums.RideStatus.ACCEPTED
ORDER BY rp.date ASC, rp.time ASC
""")
    List<RidePlan> findUpcomingAcceptedRide(@Param("driverId") Long driverId, Pageable pageable);

    @EntityGraph(attributePaths = {
            "user",
            "user.basicInformation",
            "driver",
            "driver.basicInformation",
            "role",
            "rideType"
    })
    @Query("""
SELECT rp
FROM RidePlan rp
WHERE (
    (:type = 'PARTNER' AND rp.user.id = :userId)
    OR
    (:type = 'DRIVER' AND rp.driver.id = :userId)
)
AND (:status IS NULL OR rp.rideStatus = :status)
ORDER BY rp.createdAt DESC
""")
    Page<RidePlan> findRidePlansByUserTypeAndStatus(
            @Param("userId") Long userId,
            @Param("type") String type,
            @Param("status") RideStatus status,
            Pageable pageable
    );
}

