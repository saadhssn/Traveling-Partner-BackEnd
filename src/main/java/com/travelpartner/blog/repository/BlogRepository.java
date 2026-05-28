package com.travelpartner.blog.repository;

import com.travelpartner.blog.enums.BlogStatus;
import com.travelpartner.blog.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    Optional<Blog> findByIdAndIsDeletedFalse(Long id);

    Page<Blog> findByIsDeletedFalse(Pageable pageable);

    Page<Blog> findByCategoryIdAndIsDeletedFalse(Long categoryId, Pageable pageable);

    @Query("""
    SELECT b FROM Blog b
    WHERE b.isDeleted = false
    AND (:categoryId IS NULL OR b.category.id = :categoryId)
    AND (
        CAST(:search AS text) IS NULL OR
        b.mainTitle ILIKE CONCAT('%', CAST(:search AS text), '%') OR
        b.seoTitle ILIKE CONCAT('%', CAST(:search AS text), '%') OR
        b.author ILIKE CONCAT('%', CAST(:search AS text), '%')
    )
    AND (:status IS NULL OR b.status = :status)
    AND (:date IS NULL OR b.date = :date)
    ORDER BY b.id DESC
""")
    Page<Blog> filterBlogs(
            @Param("search") String search,
            @Param("categoryId") Long categoryId,
            @Param("status") BlogStatus status,
            @Param("date") LocalDate date,
            Pageable pageable
    );
}