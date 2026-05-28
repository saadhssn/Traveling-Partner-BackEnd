package com.travelpartner.banner.service.impl;

import com.travelpartner.banner.dto.BannerDto;
import com.travelpartner.banner.model.Banner;
import com.travelpartner.banner.repository.BannerRepository;
import com.travelpartner.banner.service.BannerService;
import com.travelpartner.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;

    private BannerDto mapToDto(Banner banner) {
        return BannerDto.builder()
                .id(banner.getId())
                .bannerImage(banner.getBannerImage())
                .bannerTitle(banner.getBannerTitle())
                .bannerDescription(banner.getBannerDescription())
                .build();
    }

    private Banner mapToEntity(BannerDto dto) {
        return Banner.builder()
                .bannerImage(dto.getBannerImage())
                .bannerTitle(dto.getBannerTitle())
                .bannerDescription(dto.getBannerDescription())
                .build();
    }

    @Override
    public ApiResponse<BannerDto> createBanner(BannerDto dto) {

        Banner banner = bannerRepository.save(mapToEntity(dto));
        return ApiResponse.success("Banner created successfully", mapToDto(banner));
    }

    @Override
    public ApiResponse<Page<BannerDto>> getAllBanners(int page, int size, String title) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Banner> bannerPage;

        if (title != null && !title.trim().isEmpty()) {
            bannerPage = bannerRepository.findByTitleContaining(title, pageable);
        } else {
            bannerPage = bannerRepository.findAll(pageable);
        }

        Page<BannerDto> dtoPage = bannerPage.map(this::mapToDto);

        return ApiResponse.success("Banners fetched successfully", dtoPage);
    }

    @Override
    public ApiResponse<BannerDto> getBannerById(Long id) {
        return bannerRepository.findById(id)
                .map(banner -> ApiResponse.success(200, "Banner fetched successfully", mapToDto(banner)))
                .orElse(ApiResponse.error(404, "Banner not found"));
    }

    @Override
    public ApiResponse<BannerDto> updateBanner(Long id, BannerDto dto) {

        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Banner not found"));

        banner.setBannerImage(dto.getBannerImage());
        banner.setBannerTitle(dto.getBannerTitle());
        banner.setBannerDescription(dto.getBannerDescription());

        Banner updated = bannerRepository.save(banner);

        return ApiResponse.success("Banner updated successfully", mapToDto(updated));
    }

    @Override
    public ApiResponse<Void> deleteBanner(Long id) {

        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Banner not found"));

        bannerRepository.delete(banner);

        return ApiResponse.success("Banner deleted successfully", null);
    }

    @Override
    public ApiResponse<Page<BannerDto>> getCarouselBanners(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<BannerDto> result = bannerRepository.findAll(pageable)
                .map(this::mapToDto);

        return ApiResponse.success("Carousel banners fetched successfully", result);
    }
}