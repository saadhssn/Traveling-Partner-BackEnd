package com.travelpartner.blog.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.blog.dto.BlogDto;
import com.travelpartner.blog.service.BlogService;
import com.travelpartner.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/website/blog")
@RequiredArgsConstructor
public class WebsiteBlogController {

    private final BlogService blogService;

    @GetMapping("/list")
    @AuditAction(action = "READ", module = "WEBSITE_BLOG", description = "Get website blogs")
    public ApiResponse<Page<BlogDto>> getBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status
    ) {
        return blogService.getWebsiteBlogs(page, size, categoryId, search, status);
    }

    @GetMapping("/view/{id}")
    @AuditAction(action = "READ", module = "WEBSITE_BLOG", description = "Get website blog by id")
    public ApiResponse<BlogDto> getBlog(@PathVariable Long id) {
        return blogService.getWebsiteBlogById(id);
    }
}