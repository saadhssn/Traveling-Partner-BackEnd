package com.travelpartner.brand.service.impl;

import com.travelpartner.brand.dto.BrandDto;
import com.travelpartner.brand.enums.BrandStatus;
import com.travelpartner.brand.model.Brand;
import com.travelpartner.brand.repository.BrandRepository;
import com.travelpartner.brand.service.BrandService;
import com.travelpartner.color.model.Color;
import com.travelpartner.color.repository.ColorRepository;
import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.modelnumber.model.ModelNumber;
import com.travelpartner.modelnumber.repository.ModelNumberRepository;
import com.travelpartner.vehicle.model.Vehicle;
import com.travelpartner.vehicletype.model.VehicleType;
import com.travelpartner.vehicletype.repository.VehicleTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository repository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final ModelNumberRepository modelNumberRepository;
    private final ColorRepository colorRepository;

    @Override
    public ApiResponse<BrandDto> create(BrandDto dto) {

        VehicleType vehicleType = vehicleTypeRepository.findById(dto.getVehicleTypeId())
                .orElseThrow(() -> new RuntimeException("VehicleType not found"));

        Brand brand = Brand.builder()
                .name(dto.getName())
                .image(dto.getImage())
                .status(dto.getStatus() != null
                        ? dto.getStatus()
                        : BrandStatus.ACTIVE)
                .vehicleType(vehicleType)
                .build();

        Brand saved = repository.save(brand);

        return ApiResponse.success(
                "Brand created successfully",
                mapToDto(saved)
        );
    }

    @Override
    public ApiResponse<Page<BrandDto>> getAll(int page, int size, String search) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Brand> brands;

        if (search != null && !search.isEmpty()) {
            brands = repository.findByNameContainingIgnoreCaseAndIdNotNull(search, pageable);
        } else {
            brands = repository.findAll(pageable);
        }

        Page<BrandDto> result = brands.map(this::mapToDto);

        return ApiResponse.success("Brand list", result);
    }

    @Override
    public ApiResponse<BrandDto> getById(Long id) {

        Brand brand = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        return ApiResponse.success("Brand found", mapToDto(brand));
    }

    @Override
    public ApiResponse<BrandDto> update(Long id, BrandDto dto) {

        Brand brand = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        if (dto.getName() != null) {
            brand.setName(dto.getName());
        }

        if (dto.getImage() != null) {
            brand.setImage(dto.getImage());
        }

        if (dto.getStatus() != null) {
            brand.setStatus(dto.getStatus());
        }

        if (dto.getVehicleTypeId() != null) {

            VehicleType vehicleType =
                    vehicleTypeRepository.findById(dto.getVehicleTypeId())
                            .orElseThrow(() ->
                                    new RuntimeException("VehicleType not found"));

            brand.setVehicleType(vehicleType);
        }

        Brand updated = repository.save(brand);

        return ApiResponse.success(
                "Brand updated successfully",
                mapToDto(updated)
        );
    }

    @Override
    public ApiResponse<Void> delete(Long id) {

        Brand brand = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        repository.delete(brand);

        return ApiResponse.success("Brand deleted successfully", null);
    }

    private BrandDto mapToDto(Brand entity) {

        return BrandDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .image(entity.getImage())
                .status(entity.getStatus())
                .vehicleTypeId(entity.getVehicleType().getId())
                .build();
    }
}
