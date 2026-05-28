package com.travelpartner.availabletaxi.repository;

import com.travelpartner.availabletaxi.enums.DriverStatus;
import com.travelpartner.availabletaxi.enums.Gender;
import com.travelpartner.availabletaxi.model.AvailableTaxi;
import com.travelpartner.basicinformation.model.BasicInformation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailableTaxiRepository extends JpaRepository<AvailableTaxi, Long> {

    boolean existsByDriver_Id(Long driverId);

    Optional<AvailableTaxi> findByDriver_Id(Long driverId);

    List<AvailableTaxi> findByGenderAndCityAndDriverStatus(
            Gender gender,
            String city,
            DriverStatus driverStatus
    );

    @Query("""
SELECT a FROM AvailableTaxi a
WHERE a.driverStatus = com.travelpartner.availabletaxi.enums.DriverStatus.ONLINE
AND (:gender IS NULL OR a.gender = :gender)
AND (:city IS NULL OR a.city = :city)
""")
    Page<AvailableTaxi> findOnlineDrivers(
            @Param("gender") Gender gender,
            @Param("city") String city,
            Pageable pageable
    );

    List<AvailableTaxi> findByDriverStatus(DriverStatus driverStatus);

    @Query("""
SELECT a FROM AvailableTaxi a
WHERE a.driverStatus = com.travelpartner.availabletaxi.enums.DriverStatus.ONLINE
AND a.onlineSince IS NOT NULL
AND a.onlineSince <= :time
AND (a.awaitingConfirmation = false OR a.awaitingConfirmation IS NULL)
""")
    List<AvailableTaxi> findDriversNeedingConfirmation(
            @Param("time") LocalDateTime time
    );

    @Query("""
SELECT a FROM AvailableTaxi a
WHERE a.awaitingConfirmation = true
AND a.confirmationSentAt <= :time
""")
    List<AvailableTaxi> findDriversForAutoOffline(
            @Param("time") LocalDateTime time
    );
}