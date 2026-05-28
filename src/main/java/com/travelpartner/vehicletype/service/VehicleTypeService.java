package com.travelpartner.vehicletype.service;

import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.vehicletype.dto.VehicleTypeDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VehicleTypeService {

    ApiResponse<VehicleTypeDto> create(VehicleTypeDto dto);

    ApiResponse<Page<VehicleTypeDto>> getAllWithPagination(String search, int page, int size);

    ApiResponse<VehicleTypeDto> getById(Long id);

    ApiResponse<VehicleTypeDto> update(Long id, VehicleTypeDto dto);

    ApiResponse<Void> delete(Long id);
}
