package com.travelpartner.blog.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.blog.dto.BlogDto;
import com.travelpartner.blog.service.BlogService;
import com.travelpartner.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blog")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @PostMapping("/create")
    @AuditAction(action = "CREATE", module = "BLOG", description = "Create blog")
    public ApiResponse<BlogDto> create(@RequestBody BlogDto dto) {
        return blogService.create(dto);
    }

    @GetMapping("/getAll")
    @AuditAction(action = "READ", module = "BLOG", description = "Get all blogs")
    public ApiResponse<Page<BlogDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status
    ) {
        return blogService.getAll(page, size, search, status);
    }

    @GetMapping("/getById/{id}")
    @AuditAction(action = "READ", module = "BLOG", description = "Get blog by id")
    public ApiResponse<BlogDto> getById(@PathVariable Long id) {
        return blogService.getById(id);
    }

    @PutMapping("/update/{id}")
    @AuditAction(action = "UPDATE", module = "BLOG", description = "Update blog")
    public ApiResponse<BlogDto> update(@PathVariable Long id,
                                       @RequestBody BlogDto dto) {
        return blogService.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    @AuditAction(action = "DELETE", module = "BLOG", description = "Delete blog")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        return blogService.delete(id);
    }
}