package com.travelpartner.modelnumber.repository;

import com.travelpartner.modelnumber.model.ModelNumber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelNumberRepository extends JpaRepository<ModelNumber, Long> {

    Page<ModelNumber> findByNameContainingIgnoreCase(String name, Pageable pageable);
}