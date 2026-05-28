package com.travelpartner.brand.repository;

import com.travelpartner.brand.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    Page<Brand> findByNameContainingIgnoreCaseAndIdNotNull(String name, Pageable pageable);

}
