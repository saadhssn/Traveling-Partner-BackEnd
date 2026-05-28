package com.travelpartner.blogcategory.service;

import com.travelpartner.blogcategory.dto.BlogCategoryDto;
import com.travelpartner.blogcategory.dto.BlogCategoryNameDto;
import com.travelpartner.common.response.ApiResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BlogCategoryService {

    ApiResponse<BlogCategoryDto> create(BlogCategoryDto dto);

    ApiResponse<Page<BlogCategoryDto>> getAll(int page, int size);

    ApiResponse<BlogCategoryDto> getById(Long id);

    ApiResponse<BlogCategoryDto> update(Long id, BlogCategoryDto dto);

    ApiResponse<Void> delete(Long id);

    ApiResponse<List<BlogCategoryNameDto>> getAllNames();
}