package com.travelpartner.banner.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.banner.dto.BannerDto;
import com.travelpartner.banner.service.BannerService;
import com.travelpartner.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @PostMapping("/create")
    @AuditAction(action = "CREATE", module = "BANNER", description = "Create banner")
    public ApiResponse<BannerDto> create(@RequestBody BannerDto dto) {
        return bannerService.createBanner(dto);
    }

    @GetMapping("/getAll")
    @AuditAction(action = "READ", module = "BANNER", description = "Get all banners paginated with search")
    public ApiResponse<Page<BannerDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title
    ) {
        return bannerService.getAllBanners(page, size, title);
    }

    @GetMapping("/getById/{id}")
    @AuditAction(action = "READ", module = "BANNER", description = "Get banner by id")
    public ApiResponse<BannerDto> getById(@PathVariable Long id) {
        return bannerService.getBannerById(id);
    }

    @PutMapping("/update/{id}")
    @AuditAction(action = "UPDATE", module = "BANNER", description = "Update banner")
    public ApiResponse<BannerDto> update(@PathVariable Long id,
                                         @RequestBody BannerDto dto) {
        return bannerService.updateBanner(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    @AuditAction(action = "DELETE", module = "BANNER", description = "Delete banner")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        return bannerService.deleteBanner(id);
    }

    @GetMapping("/carousel")
    @AuditAction(action = "READ", module = "BANNER", description = "Get carousel banners")
    public ApiResponse<Page<BannerDto>> getCarouselBanners(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return bannerService.getCarouselBanners(page, size);
    }
}