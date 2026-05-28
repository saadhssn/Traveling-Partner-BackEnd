package com.travelpartner.license.service;

import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.license.dto.LicenseDto;
import org.springframework.data.domain.Page;

public interface LicenseService {

    ApiResponse<LicenseDto> create(LicenseDto dto);

    ApiResponse<Page<LicenseDto>> getAll(int page, int size);

    ApiResponse<Page<LicenseDto>> getAllFiltered(int page, int size, Boolean verified);

    ApiResponse<LicenseDto> getById(Long id);

    ApiResponse<LicenseDto> update(Long id, LicenseDto dto);

    ApiResponse<LicenseDto> verifyLicense(Long id, LicenseDto dto);

    ApiResponse<Void> delete(Long id);
}
