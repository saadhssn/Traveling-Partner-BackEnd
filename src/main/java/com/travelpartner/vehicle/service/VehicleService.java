package com.travelpartner.vehicle.service;

import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.vehicle.dto.VehicleDto;
import org.springframework.data.domain.Page;

public interface VehicleService {

    ApiResponse<VehicleDto> create(VehicleDto dto);

    ApiResponse<Page<VehicleDto>> getAll(int page, int size);

    ApiResponse<VehicleDto> getById(Long id);

    ApiResponse<VehicleDto> update(Long id, VehicleDto dto);

    ApiResponse<Void> delete(Long id);

    ApiResponse<VehicleDto> verifyVehicle(Long id);
}
