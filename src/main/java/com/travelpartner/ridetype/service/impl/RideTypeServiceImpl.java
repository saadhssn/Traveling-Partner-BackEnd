package com.travelpartner.ridetype.service.impl;

import com.travelpartner.ridetype.dto.RideTypeDto;
import com.travelpartner.ridetype.model.RideType;
import com.travelpartner.ridetype.repository.RideTypeRepository;
import com.travelpartner.ridetype.service.RideTypeService;
import com.travelpartner.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
public class RideTypeServiceImpl implements RideTypeService {

    private final RideTypeRepository rideTypeRepository;

    // Entity -> DTO
    private RideTypeDto mapToDto(RideType rideType) {
        return RideTypeDto.builder()
                .id(rideType.getId())
                .name(rideType.getName())
                .build();
    }

    // DTO -> Entity
    private RideType mapToEntity(RideTypeDto dto) {
        return RideType.builder()
                .name(dto.getName())
                .build();
    }

    @Override
    public ApiResponse<RideTypeDto> createRideType(RideTypeDto dto) {

        if (rideTypeRepository.existsByName(dto.getName())) {
            return ApiResponse.error(400, "RideType with this name already exists");
        }

        RideType rideType = mapToEntity(dto);

        RideType saved = rideTypeRepository.save(rideType);

        return ApiResponse.success("RideType created successfully", mapToDto(saved));
    }

    @Override
    public ApiResponse<List<RideTypeDto>> getAllRideTypes() {
        List<RideTypeDto> dtos = rideTypeRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ApiResponse.success("RideTypes fetched successfully", dtos);
    }

    @Override
    public ApiResponse<RideTypeDto> getRideTypeById(Long id) {
        return rideTypeRepository.findById(id)
                .map(rideType -> ApiResponse.<RideTypeDto>builder()
                        .success(true)
                        .statusCode(200)
                        .message("RideType fetched successfully")
                        .data(mapToDto(rideType))
                        .timestamp(LocalDateTime.now())
                        .build()
                )
                .orElse(ApiResponse.<RideTypeDto>builder()
                        .success(false)
                        .statusCode(404)
                        .message("RideType not found")
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build()
                );
    }

    @Override
    public ApiResponse<RideTypeDto> updateRideType(Long id, RideTypeDto dto) {
        RideType rideType = rideTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("RideType not found"));

        rideType.setName(dto.getName());
        RideType updated = rideTypeRepository.save(rideType);
        return ApiResponse.success("RideType updated successfully", mapToDto(updated));
    }

    @Override
    public ApiResponse<Void> deleteRideType(Long id) {
        RideType rideType = rideTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("RideType not found"));

        rideTypeRepository.delete(rideType);
        return ApiResponse.success("RideType deleted successfully", null);
    }
}