package com.travelpartner.license.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.license.dto.LicenseDto;
import com.travelpartner.license.service.LicenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/licenses")
@RequiredArgsConstructor
public class LicenseController {

    private final LicenseService service;

    @PostMapping("/create")
    @AuditAction(action = "CREATE", module = "LICENSE", description = "Create license")
    public ApiResponse<LicenseDto> create(@Valid @RequestBody LicenseDto dto) {
        return service.create(dto);
    }

    @GetMapping("/getAll")
    @AuditAction(action = "READ", module = "LICENSE", description = "Get all licenses")
    public ApiResponse<Page<LicenseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Boolean verified) {

        if (verified == null) {
            return service.getAll(page, size);
        } else {
            return service.getAllFiltered(page, size, verified);
        }
    }

    @GetMapping("/getById/{id}")
    @AuditAction(action = "READ", module = "LICENSE", description = "Get license by id")
    public ApiResponse<LicenseDto> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/update/{id}")
    @AuditAction(action = "UPDATE", module = "LICENSE", description = "Update license")
    public ApiResponse<LicenseDto> update(@PathVariable Long id,
                                          @Valid @RequestBody LicenseDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    @AuditAction(action = "DELETE", module = "LICENSE", description = "Delete license")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        return service.delete(id);
    }

    // Admin-only endpoint
//    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/verify/{id}")
    @AuditAction(action = "UPDATE", module = "LICENSE", description = "Verify license")
    public ApiResponse<LicenseDto> verifyLicense(@PathVariable Long id,
                                                 @Valid @RequestBody LicenseDto dto) {
        return service.verifyLicense(id, dto);
    }
}