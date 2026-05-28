package com.travelpartner.brand.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.brand.dto.BrandDto;
import com.travelpartner.brand.service.BrandService;
import com.travelpartner.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService service;

    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    @AuditAction(action = "CREATE", module = "BRAND", description = "Create brand")
    public ApiResponse<BrandDto> create(@Valid @RequestBody BrandDto dto) {
        return service.create(dto);
    }

    @GetMapping("/getAll")
    @AuditAction(action = "READ", module = "BRAND", description = "Get all brands")
    public ApiResponse<Page<BrandDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {

        return service.getAll(page, size, search);
    }

    @GetMapping("/getById/{id}")
    @AuditAction(action = "READ", module = "BRAND", description = "Get brand by id")
    public ApiResponse<BrandDto> getById(@PathVariable Long id) {
        return service.getById(id);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    @AuditAction(action = "UPDATE", module = "BRAND", description = "Update brand")
    public ApiResponse<BrandDto> update(@PathVariable Long id,
                                        @Valid @RequestBody BrandDto dto) {
        return service.update(id, dto);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    @AuditAction(action = "DELETE", module = "BRAND", description = "Delete brand")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
