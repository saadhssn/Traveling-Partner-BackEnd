package com.travelpartner.ridetype.repository;

import com.travelpartner.ridetype.model.RideType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RideTypeRepository extends JpaRepository<RideType, Long> {

    boolean existsByName(String name);

    Optional<RideType> findByName(String name);
}