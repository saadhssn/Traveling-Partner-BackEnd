package com.travelpartner.blog.service;

import com.travelpartner.blog.dto.BlogDto;
import com.travelpartner.common.response.ApiResponse;
import org.springframework.data.domain.Page;

public interface BlogService {

    ApiResponse<BlogDto> create(BlogDto dto);

    ApiResponse<Page<BlogDto>> getAll(
            int page,
            int size,
            String search,
            String status
    );

    ApiResponse<BlogDto> getById(Long id);

    ApiResponse<BlogDto> update(Long id, BlogDto dto);

    ApiResponse<Void> delete(Long id);

    ApiResponse<Page<BlogDto>> getWebsiteBlogs(
            int page,
            int size,
            Long categoryId,
            String search,
            String status
    );

    ApiResponse<BlogDto> getWebsiteBlogById(Long id);
}