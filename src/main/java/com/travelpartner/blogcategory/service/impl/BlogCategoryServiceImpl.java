package com.travelpartner.blogcategory.service.impl;

import com.travelpartner.blogcategory.dto.BlogCategoryDto;
import com.travelpartner.blogcategory.dto.BlogCategoryNameDto;
import com.travelpartner.blogcategory.model.BlogCategory;
import com.travelpartner.blogcategory.repository.BlogCategoryRepository;
import com.travelpartner.blogcategory.service.BlogCategoryService;
import com.travelpartner.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogCategoryServiceImpl implements BlogCategoryService {

    private final BlogCategoryRepository repository;

    @Override
    public ApiResponse<BlogCategoryDto> create(BlogCategoryDto dto) {
        BlogCategory category = BlogCategory.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();

        BlogCategory saved = repository.save(category);
        return ApiResponse.success("Blog category created successfully", mapToDto(saved));
    }

    @Override
    public ApiResponse<Page<BlogCategoryDto>> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BlogCategoryDto> result = repository.findAll(pageable)
                .map(this::mapToDto);
        return ApiResponse.success("Blog categories list", result);
    }

    @Override
    public ApiResponse<BlogCategoryDto> getById(Long id) {
        BlogCategory category = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog category not found"));
        return ApiResponse.success("Blog category found", mapToDto(category));
    }

    @Override
    public ApiResponse<BlogCategoryDto> update(Long id, BlogCategoryDto dto) {
        BlogCategory category = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog category not found"));

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        return ApiResponse.success("Blog category updated", mapToDto(repository.save(category)));
    }

    @Override
    public ApiResponse<Void> delete(Long id) {
        BlogCategory category = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog category not found"));

        repository.delete(category);
        return ApiResponse.success("Blog category deleted", null);
    }

    private BlogCategoryDto mapToDto(BlogCategory entity) {
        return BlogCategoryDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

    @Override
    public ApiResponse<List<BlogCategoryNameDto>> getAllNames() {

        List<BlogCategoryNameDto> result = repository.findAll()
                .stream()
                .map(cat -> BlogCategoryNameDto.builder()
                        .id(cat.getId())
                        .name(cat.getName())
                        .build()
                )
                .toList();

        return ApiResponse.success("Blog category names list", result);
    }
}