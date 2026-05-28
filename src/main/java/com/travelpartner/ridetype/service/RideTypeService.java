package com.travelpartner.ridetype.service;

import com.travelpartner.ridetype.dto.RideTypeDto;
import com.travelpartner.common.response.ApiResponse;

import java.util.List;

public interface RideTypeService {

    ApiResponse<RideTypeDto> createRideType(RideTypeDto dto);

    ApiResponse<List<RideTypeDto>> getAllRideTypes();

    ApiResponse<RideTypeDto> getRideTypeById(Long id);

    ApiResponse<RideTypeDto> updateRideType(Long id, RideTypeDto dto);

    ApiResponse<Void> deleteRideType(Long id);
}