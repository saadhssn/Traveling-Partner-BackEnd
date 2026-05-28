package com.travelpartner.newsletter.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.newsletter.dto.NewsletterDto;
import com.travelpartner.newsletter.service.NewsletterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/newsletter")
@RequiredArgsConstructor
public class NewsletterController {

    private final NewsletterService newsletterService;

    @PostMapping("/create")
    @AuditAction(action = "CREATE", module = "NEWSLETTER", description = "Create newsletter")
    public ApiResponse<NewsletterDto> create(
            @RequestBody NewsletterDto dto
    ) {
        return newsletterService.create(dto);
    }

    @GetMapping("/getAll")
    @AuditAction(action = "READ", module = "NEWSLETTER", description = "Get all newsletters")
    public ApiResponse<Page<NewsletterDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status
    ) {
        return newsletterService.getAll(
                page,
                size,
                search,
                status
        );
    }

    @GetMapping("/getById/{id}")
    @AuditAction(action = "READ", module = "NEWSLETTER", description = "Get newsletter by id")
    public ApiResponse<NewsletterDto> getById(
            @PathVariable Long id
    ) {
        return newsletterService.getById(id);
    }

    @PutMapping("/update/{id}")
    @AuditAction(action = "UPDATE", module = "NEWSLETTER", description = "Update newsletter")
    public ApiResponse<NewsletterDto> update(
            @PathVariable Long id,
            @RequestBody NewsletterDto dto
    ) {
        return newsletterService.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    @AuditAction(action = "DELETE", module = "NEWSLETTER", description = "Delete newsletter")
    public ApiResponse<Void> delete(
            @PathVariable Long id
    ) {
        return newsletterService.delete(id);
    }

}