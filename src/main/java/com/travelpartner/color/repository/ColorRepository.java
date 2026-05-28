package com.travelpartner.color.repository;

import com.travelpartner.color.model.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color, Long> {

    Page<Color> findByNameContainingIgnoreCase(String search, Pageable pageable);
}