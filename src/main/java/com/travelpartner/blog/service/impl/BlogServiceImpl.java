package com.travelpartner.blog.service.impl;

import com.travelpartner.blog.dto.BlogDto;
import com.travelpartner.blog.enums.BlogStatus;
import com.travelpartner.blog.model.Blog;
import com.travelpartner.blog.repository.BlogRepository;
import com.travelpartner.blogcategory.model.BlogCategory;
import com.travelpartner.blogcategory.repository.BlogCategoryRepository;
import com.travelpartner.blog.service.BlogService;
import com.travelpartner.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final BlogCategoryRepository categoryRepository;

    /* ================= CREATE ================= */

    @Override
    public ApiResponse<BlogDto> create(BlogDto dto) {

        validateDescriptionLength(dto.getDescription1(), dto.getDescription2());

        BlogCategory category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        }

        Blog blog = Blog.builder()
                .coverImage(dto.getCoverImage())
                .mainTitle(dto.getMainTitle())
                .seoTitle(dto.getSeoTitle())
                .seoDescription(dto.getSeoDescription())
                .status(dto.getStatus() != null
                        ? BlogStatus.valueOf(dto.getStatus().toUpperCase())
                        : BlogStatus.DRAFT)
                .description1(dto.getDescription1())
                .description2(dto.getDescription2())
                .date(dto.getDate())
                .author(dto.getAuthor())
                .readTime(dto.getReadTime())
                .tags(dto.getTags())
                .category(category)
                .isDeleted(false)
                .build();

        return ApiResponse.success(
                "Blog created successfully",
                mapToDto(blogRepository.save(blog))
        );
    }

    /* ================= GET ALL ================= */

    @Override
    public ApiResponse<Page<BlogDto>> getAll(
            int page,
            int size,
            String search,
            String status
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        search = (search == null || search.trim().isEmpty()) ? null : search.trim();
        status = (status == null || status.trim().isEmpty()) ? null : status.trim();

        Long categoryId = null;
        LocalDate date = null;
        String keyword = null;

        if (search != null) {
            String[] parts = search.split(" ");

            for (String part : parts) {

                try {
                    if (date == null) {
                        date = LocalDate.parse(part);
                        continue;
                    }
                } catch (Exception ignored) {}

                if (categoryId == null && part.matches("\\d+")) {
                    categoryId = Long.parseLong(part);
                    continue;
                }

                keyword = (keyword == null) ? part : keyword + " " + part;
            }
        }

        BlogStatus blogStatus = null;

        if (status != null && !status.trim().isEmpty()) {
            blogStatus = BlogStatus.valueOf(status.trim().toUpperCase());
        }

        Page<Blog> blogs = blogRepository.filterBlogs(
                keyword,
                categoryId,
                blogStatus,
                date,
                pageable
        );

        return ApiResponse.success("Blogs fetched successfully",
                blogs.map(this::mapToDto));
    }

    /* ================= GET BY ID ================= */

    @Override
    public ApiResponse<BlogDto> getById(Long id) {

        Blog blog = blogRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        return ApiResponse.success("Blog fetched successfully",
                mapToDto(blog));
    }

    /* ================= UPDATE ================= */

    @Override
    public ApiResponse<BlogDto> update(Long id, BlogDto dto) {

        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        blog.setCoverImage(dto.getCoverImage());
        blog.setMainTitle(dto.getMainTitle());
        blog.setSeoTitle(dto.getSeoTitle());
        blog.setSeoDescription(dto.getSeoDescription());

        if (dto.getStatus() != null && !dto.getStatus().trim().isEmpty()) {
            try {
                blog.setStatus(BlogStatus.valueOf(dto.getStatus().trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid blog status. Allowed: DRAFT, PUBLISHED, ARCHIVED");
            }
        }

        blog.setDescription1(dto.getDescription1());
        blog.setDescription2(dto.getDescription2());
        blog.setDate(dto.getDate());
        blog.setAuthor(dto.getAuthor());
        blog.setReadTime(dto.getReadTime());
        blog.setTags(dto.getTags());

        if (dto.getCategoryId() != null) {
            BlogCategory category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            blog.setCategory(category);
        }

        return ApiResponse.success("Blog updated successfully",
                mapToDto(blogRepository.save(blog)));
    }
    /* ================= DELETE ================= */

    @Override
    public ApiResponse<Void> delete(Long id) {

        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        blog.setIsDeleted(true);
        blog.setDeletedAt(java.time.LocalDateTime.now());

        blogRepository.save(blog);

        return ApiResponse.success("Blog deleted successfully", null);
    }

    /* ================= WEBSITE ================= */

    @Override
    public ApiResponse<Page<BlogDto>> getWebsiteBlogs(
            int page,
            int size,
            Long categoryId,
            String search,
            String status
    ) {
        return getAll(page, size, search, "PUBLISHED"); // force published
    }

    @Override
    public ApiResponse<BlogDto> getWebsiteBlogById(Long id) {

        Blog blog = blogRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        // Increment view count
        if (blog.getViews() == null) {
            blog.setViews(0L);
        }
        blog.setViews(blog.getViews() + 1);

        blogRepository.save(blog);

        return ApiResponse.success("Blog fetched successfully",
                mapToDto(blog));
    }

    /* ================= MAPPER ================= */

    private BlogDto mapToDto(Blog entity) {
        return BlogDto.builder()
                .id(entity.getId())
                .coverImage(entity.getCoverImage())
                .mainTitle(entity.getMainTitle())
                .seoTitle(entity.getSeoTitle())
                .seoDescription(entity.getSeoDescription())
                .status(entity.getStatus() != null ? entity.getStatus().name() : null)
                .description1(entity.getDescription1())
                .description2(entity.getDescription2())
                .date(entity.getDate())
                .author(entity.getAuthor())
                .readTime(entity.getReadTime())
                .tags(entity.getTags())
                .views(entity.getViews())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
                .categoryName(entity.getCategory() != null ? entity.getCategory().getName() : null)
                .build();
    }

    private void validateDescriptionLength(String description1, String description2) {

        int description1Words = 0;
        int description2Words = 0;

        // Description 1
        if (description1 != null && !description1.trim().isEmpty()) {

            description1Words = description1
                    .trim()
                    .split("\\s+").length;
        }

        // Description 2
        if (description2 != null && !description2.trim().isEmpty()) {

            String plainText = description2
                    .replaceAll("<[^>]*>", " ")
                    .replaceAll("&nbsp;", " ")
                    .trim();

            if (!plainText.isEmpty()) {
                description2Words = plainText
                        .split("\\s+").length;
            }
        }

        System.out.println("Description1 Words: " + description1Words);
        System.out.println("Description2 Words: " + description2Words);

        // Separate validations
        if (description1Words > 1500) {
            throw new RuntimeException(
                    "Description1 exceeds 1500 words limit"
            );
        }

        if (description2Words > 1500) {
            throw new RuntimeException(
                    "Description2 exceeds 1500 words limit"
            );
        }
    }
}