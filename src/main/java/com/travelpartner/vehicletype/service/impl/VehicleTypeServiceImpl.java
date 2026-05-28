package com.travelpartner.vehicletype.service.impl;

import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.vehicletype.dto.VehicleTypeDto;
import com.travelpartner.vehicletype.enums.VehicleTypeStatus;
import com.travelpartner.vehicletype.model.VehicleType;
import com.travelpartner.vehicletype.repository.VehicleTypeRepository;
import com.travelpartner.vehicletype.service.VehicleTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleTypeServiceImpl implements VehicleTypeService {

    private final VehicleTypeRepository repository;

    @Override
    public ApiResponse<VehicleTypeDto> create(VehicleTypeDto dto) {

        VehicleType entity = VehicleType.builder()
                .name(dto.getName())
                .image(dto.getImage())
                .status(dto.getStatus())
                .status(dto.getStatus() != null ? dto.getStatus() : VehicleTypeStatus.ACTIVE)
                .build();

        return ApiResponse.success("Vehicle type created",
                map(repository.save(entity)));
    }

    @Override
    public ApiResponse<Page<VehicleTypeDto>> getAllWithPagination(String search, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<VehicleType> vehicleTypePage;

        if (search != null && !search.isBlank()) {
            vehicleTypePage = repository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            vehicleTypePage = repository.findAll(pageable);
        }

        Page<VehicleTypeDto> dtoPage = vehicleTypePage.map(this::map);

        return ApiResponse.success("Vehicle type list with pagination", dtoPage);
    }

    @Override
    public ApiResponse<VehicleTypeDto> getById(Long id) {
        VehicleType entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("VehicleType not found"));

        return ApiResponse.success("Vehicle type found", map(entity));
    }

    @Override
    public ApiResponse<VehicleTypeDto> update(Long id, VehicleTypeDto dto) {
        VehicleType entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("VehicleType not found"));

        entity.setName(dto.getName());
        entity.setImage(dto.getImage());
        entity.setStatus(dto.getStatus());

        return ApiResponse.success("Vehicle type updated",
                map(repository.save(entity)));
    }

    @Override
    public ApiResponse<Void> delete(Long id) {
        repository.deleteById(id);
        return ApiResponse.success("Vehicle type deleted", null);
    }

    private VehicleTypeDto map(VehicleType e) {
        return VehicleTypeDto.builder()
                .id(e.getId())
                .name(e.getName())
                .image(e.getImage())
                .status(e.getStatus())
                .build();
    }
}
