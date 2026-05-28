package com.travelpartner.rideplan.service;

import com.travelpartner.rideplan.dto.RidePlanDto;
import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.rideplan.enums.RideStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RidePlanService {

    ApiResponse<RidePlanDto> createRidePlan(RidePlanDto dto);

    ApiResponse<Page<RidePlanDto>> getAllRidePlans(int page, int size);

    ApiResponse<RidePlanDto> getRidePlanById(Long id);

    ApiResponse<RidePlanDto> updateRidePlan(Long id, RidePlanDto dto);

    ApiResponse<Void> deleteRidePlan(Long id);

    ApiResponse<RidePlanDto> updateRideStatus(Long id, RideStatus status, Long driverId);

    ApiResponse<Page<RidePlanDto>> getRidePlansByDriverAndStatus(
            Long driverId,
            RideStatus status,
            int page,
            int size
    );

    ApiResponse<RidePlanDto> getUpcomingRide(Long driverId);

    ApiResponse<Page<RidePlanDto>> getRidePlansByUserTypeAndStatus(
            Long userId,
            String type,
            RideStatus status,
            int page,
            int size
    );
}