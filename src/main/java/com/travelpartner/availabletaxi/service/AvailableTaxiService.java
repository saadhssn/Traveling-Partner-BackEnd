package com.travelpartner.availabletaxi.service;

import com.travelpartner.availabletaxi.dto.AvailableTaxiDto;
import com.travelpartner.availabletaxi.dto.AvailableTaxiFullDto;
import com.travelpartner.availabletaxi.enums.DriverStatus;
import com.travelpartner.availabletaxi.enums.Gender;
import com.travelpartner.common.response.ApiResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AvailableTaxiService {

    ApiResponse<AvailableTaxiDto> createAvailableTaxi(AvailableTaxiDto dto);

    ApiResponse<List<AvailableTaxiDto>> getAllAvailableTaxis();

    ApiResponse<AvailableTaxiFullDto> getAvailableTaxiById(Long id);

    ApiResponse<AvailableTaxiDto> updateDriverStatus(Long id, AvailableTaxiDto dto);

    ApiResponse<Void> deleteAvailableTaxi(Long id);

    // Fetch available taxis filtered by gender, city, and online status
    ApiResponse<Page<AvailableTaxiFullDto>> getAvailableTaxisByGenderAndCity(
            Gender gender,
            String city,
            int page,
            int size
    );

    ApiResponse<String> confirmOnline(
            Long driverId,
            boolean online
    );
}