package com.travelpartner.banner.repository;

import com.travelpartner.banner.model.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {

    @Query("SELECT b FROM Banner b WHERE LOWER(b.bannerTitle) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Banner> findByTitleContaining(@Param("title") String title, Pageable pageable);

    Page<Banner> findAll(Pageable pageable);
}