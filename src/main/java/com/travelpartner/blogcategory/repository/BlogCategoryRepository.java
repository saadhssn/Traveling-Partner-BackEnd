package com.travelpartner.blogcategory.repository;

import com.travelpartner.blogcategory.model.BlogCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogCategoryRepository extends JpaRepository<BlogCategory, Long> {
}