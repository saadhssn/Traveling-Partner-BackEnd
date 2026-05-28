package com.travelpartner.newsletter.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.newsletter.dto.NewsletterDto;
import com.travelpartner.newsletter.service.NewsletterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/website/newsletter")
@RequiredArgsConstructor
public class WebsiteNewsletterController {

    private final NewsletterService newsletterService;

    @GetMapping("/list")
    @AuditAction(action = "READ", module = "WEBSITE_NEWSLETTER", description = "Get website newsletters")
    public ApiResponse<Page<NewsletterDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return newsletterService.getAllForWebsite(page, size);
    }

    @GetMapping("/view/{id}")
    @AuditAction(action = "READ", module = "WEBSITE_NEWSLETTER", description = "Get website newsletter by id")
    public ApiResponse<NewsletterDto> getById(
            @PathVariable Long id
    ) {
        return newsletterService.getByIdForWebsite(id);
    }

}