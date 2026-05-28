package com.travelpartner.brand.service;

import com.travelpartner.brand.dto.BrandDto;
import com.travelpartner.common.response.ApiResponse;
import org.springframework.data.domain.Page;

public interface BrandService {

    ApiResponse<BrandDto> create(BrandDto dto);

    ApiResponse<Page<BrandDto>> getAll(int page, int size, String search);

    ApiResponse<BrandDto> getById(Long id);

    ApiResponse<BrandDto> update(Long id, BrandDto dto);

    ApiResponse<Void> delete(Long id);
}
