package com.travelpartner.contactus.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.contactus.dto.ContactUsDto;
import com.travelpartner.contactus.service.ContactUsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app/contact")
@RequiredArgsConstructor
public class AppContactUsController {

    private final ContactUsService service;

    @PostMapping("/submit")
    @AuditAction(
            action = "CREATE",
            module = "APP_CONTACT_US",
            description = "App user submitted contact form"
    )
    public ApiResponse<Void> submitContact(
            @RequestBody ContactUsDto dto
    ) {

        return service.submitFromApp(dto);
    }
}