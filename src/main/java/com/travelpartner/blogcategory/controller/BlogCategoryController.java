package com.travelpartner.blogcategory.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.blogcategory.dto.BlogCategoryDto;
import com.travelpartner.blogcategory.dto.BlogCategoryNameDto;
import com.travelpartner.blogcategory.model.BlogCategory;
import com.travelpartner.blogcategory.service.BlogCategoryService;
import com.travelpartner.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogCategory")
@RequiredArgsConstructor
public class BlogCategoryController {

    private final BlogCategoryService service;

    @PostMapping("/create")
    @AuditAction(action = "CREATE", module = "BLOG_CATEGORY", description = "Create blog category")
    public ApiResponse<BlogCategoryDto> create(@Valid @RequestBody BlogCategoryDto dto) {
        return service.create(dto);
    }

    @GetMapping("/getAll")
    @AuditAction(action = "READ", module = "BLOG_CATEGORY", description = "Get all blog categories")
    public ApiResponse<Page<BlogCategoryDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size) {
        return service.getAll(page, size);
    }

    @GetMapping("/getById/{id}")
    @AuditAction(action = "READ", module = "BLOG_CATEGORY", description = "Get blog category by id")
    public ApiResponse<BlogCategoryDto> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/update/{id}")
    @AuditAction(action = "UPDATE", module = "BLOG_CATEGORY", description = "Update blog category")
    public ApiResponse<BlogCategoryDto> update(@PathVariable Long id,
                                               @Valid @RequestBody BlogCategoryDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    @AuditAction(action = "DELETE", module = "BLOG_CATEGORY", description = "Delete blog category")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @GetMapping("/getAllNames")
    @AuditAction(action = "READ", module = "BLOG_CATEGORY", description = "Get all blog category names")
    public ApiResponse<List<BlogCategoryNameDto>> getAllNames() {
        return service.getAllNames();
    }
}