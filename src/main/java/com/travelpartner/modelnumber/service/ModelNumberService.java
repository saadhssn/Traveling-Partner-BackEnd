package com.travelpartner.modelnumber.service;

import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.modelnumber.dto.ModelNumberDto;
import org.springframework.data.domain.Page;

public interface ModelNumberService {

    ApiResponse<ModelNumberDto> create(ModelNumberDto dto);

    ApiResponse<Page<ModelNumberDto>> getAll(int page, int size);

    ApiResponse<Page<ModelNumberDto>> getAllFiltered(int page, int size, String search);

    ApiResponse<ModelNumberDto> getById(Long id);

    ApiResponse<ModelNumberDto> update(Long id, ModelNumberDto dto);

    ApiResponse<Void> delete(Long id);
}