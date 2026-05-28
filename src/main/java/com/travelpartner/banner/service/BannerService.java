package com.travelpartner.banner.service;

import com.travelpartner.banner.dto.BannerDto;
import com.travelpartner.common.response.ApiResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BannerService {

    ApiResponse<BannerDto> createBanner(BannerDto dto);

    ApiResponse<Page<BannerDto>> getAllBanners(int page, int size, String title);

    ApiResponse<BannerDto> getBannerById(Long id);

    ApiResponse<BannerDto> updateBanner(Long id, BannerDto dto);

    ApiResponse<Void> deleteBanner(Long id);

    ApiResponse<Page<BannerDto>> getCarouselBanners(int page, int size);
}