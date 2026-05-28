package com.travelpartner.vehicletype.repository;

import com.travelpartner.vehicletype.model.VehicleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long> {

    Page<VehicleType> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
