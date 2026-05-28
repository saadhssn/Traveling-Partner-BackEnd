package com.travelpartner.vehicle.repository;

import com.travelpartner.user.model.User;
import com.travelpartner.vehicle.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Page<Vehicle> findByBrandId(Long brandId, Pageable pageable);

    Page<Vehicle> findByUserId(Long userId, Pageable pageable);

    Optional<Vehicle> findByUser(User user);

    boolean existsByRegistrationNoAndIdNot(String registrationNo, Long id);

    boolean existsByRegistrationNo(String registrationNo);

    Optional<Vehicle> findByUser_Id(Long userId);
}
