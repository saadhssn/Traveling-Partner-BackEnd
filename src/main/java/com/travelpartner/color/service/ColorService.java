package com.travelpartner.color.service;

import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.color.dto.ColorDto;
import org.springframework.data.domain.Page;

public interface ColorService {

    ApiResponse<ColorDto> create(ColorDto dto);

    ApiResponse<Page<ColorDto>> getAll(int page, int size);

    ApiResponse<Page<ColorDto>> getAllFiltered(
            int page,
            int size,
            String search);

    ApiResponse<ColorDto> getById(Long id);

    ApiResponse<ColorDto> update(Long id, ColorDto dto);

    ApiResponse<Void> delete(Long id);
}