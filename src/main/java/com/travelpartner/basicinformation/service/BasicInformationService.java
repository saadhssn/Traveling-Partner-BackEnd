package com.travelpartner.basicinformation.service;

import com.travelpartner.basicinformation.dto.BasicInformationDto;
import com.travelpartner.common.response.ApiResponse;
import org.springframework.data.domain.Page;

public interface BasicInformationService {

    ApiResponse<BasicInformationDto> create(BasicInformationDto dto);

    ApiResponse<Page<BasicInformationDto>> getAll(int page, int size);

    ApiResponse<Page<BasicInformationDto>> getAllFiltered(int page, int size, Boolean deleted);

    ApiResponse<BasicInformationDto> getById(Long id);

    ApiResponse<BasicInformationDto> update(Long id, BasicInformationDto dto);

    ApiResponse<Void> delete(Long id);
}
