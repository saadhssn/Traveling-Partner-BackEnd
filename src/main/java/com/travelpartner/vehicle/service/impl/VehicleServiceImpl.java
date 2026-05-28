package com.travelpartner.vehicle.service.impl;

import com.travelpartner.brand.model.Brand;
import com.travelpartner.brand.repository.BrandRepository;
import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.modelnumber.model.ModelNumber;
import com.travelpartner.modelnumber.repository.ModelNumberRepository;
import com.travelpartner.user.model.User;
import com.travelpartner.user.repository.UserRepository;
import com.travelpartner.vehicle.dto.VehicleDto;
import com.travelpartner.vehicle.model.Vehicle;
import com.travelpartner.vehicle.repository.VehicleRepository;
import com.travelpartner.vehicle.service.VehicleService;
import com.travelpartner.user.util.RoleChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository repository;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final ModelNumberRepository modelNumberRepository;
    private final RoleChecker roleChecker;

    @Override
    public ApiResponse<VehicleDto> create(VehicleDto dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Brand brand = brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        ModelNumber modelNumber = modelNumberRepository.findById(dto.getModelNumberId())
                .orElseThrow(() -> new RuntimeException("Model number not found"));

        // ===== DUPLICATE CHECK =====
        if (dto.getRegistrationNo() != null &&
                repository.existsByRegistrationNo(dto.getRegistrationNo())) {
            throw new RuntimeException("Registration number already assigned to another vehicle");
        }

        Vehicle vehicle = Vehicle.builder()
                .modelNumber(modelNumber)
                .registrationNo(dto.getRegistrationNo())
                .registrationFront(dto.getRegistrationFront())
                .registrationBack(dto.getRegistrationBack())
                .outdoorImages(dto.getOutdoorImages())
                .indoorImages(dto.getIndoorImages())
                .ac(dto.getAc() != null ? dto.getAc() : false)
                .petsAllowed(dto.isPetsAllowed())
                .smokingAllowed(dto.isSmokingAllowed())
                .vehicleVerified(false)
                .brand(brand)
                .user(user)
                .build();

        Vehicle saved = repository.save(vehicle);

        return ApiResponse.success("Vehicle created successfully", mapToDto(saved));
    }

    @Override
    public ApiResponse<Page<VehicleDto>> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<VehicleDto> result = repository.findAll(pageable).map(this::mapToDto);
        return ApiResponse.success("Vehicle list", result);
    }

    @Override
    public ApiResponse<VehicleDto> getById(Long id) {
        Vehicle vehicle = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        return ApiResponse.success("Vehicle found", mapToDto(vehicle));
    }

    @Override
    public ApiResponse<VehicleDto> update(Long id, VehicleDto dto) {

        Vehicle vehicle = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        // ===== DUPLICATE CHECK =====
        if (dto.getRegistrationNo() != null &&
                repository.existsByRegistrationNoAndIdNot(dto.getRegistrationNo(), id)) {
            throw new RuntimeException("Registration number already assigned to another vehicle");
        }

        Brand brand = brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        ModelNumber modelNumber = modelNumberRepository.findById(dto.getModelNumberId())
                .orElseThrow(() -> new RuntimeException("Model number not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getRegistrationNo() != null)
            vehicle.setRegistrationNo(dto.getRegistrationNo());

        if (dto.getRegistrationFront() != null)
            vehicle.setRegistrationFront(dto.getRegistrationFront());

        if (dto.getRegistrationBack() != null)
            vehicle.setRegistrationBack(dto.getRegistrationBack());

        if (dto.getOutdoorImages() != null)
            vehicle.setOutdoorImages(dto.getOutdoorImages());

        if (dto.getIndoorImages() != null)
            vehicle.setIndoorImages(dto.getIndoorImages());

        if (dto.getAc() != null)
            vehicle.setAc(dto.getAc());

        vehicle.setPetsAllowed(dto.isPetsAllowed());
        vehicle.setSmokingAllowed(dto.isSmokingAllowed());

        vehicle.setModelNumber(modelNumber);
        vehicle.setBrand(brand);
        vehicle.setUser(user);

        Vehicle updated = repository.save(vehicle);

        return ApiResponse.success("Vehicle updated successfully", mapToDto(updated));
    }
    
    @Override
    public ApiResponse<Void> delete(Long id) {
        Vehicle vehicle = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        repository.delete(vehicle);
        return ApiResponse.success("Vehicle deleted successfully", null);
    }

    @Override
    public ApiResponse<VehicleDto> verifyVehicle(Long id) {
        Vehicle vehicle = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        if (!roleChecker.isAdmin(vehicle.getUser())) {
            throw new RuntimeException("Only admin can verify vehicle");
        }

        vehicle.setVehicleVerified(true);
        Vehicle saved = repository.save(vehicle);
        return ApiResponse.success("Vehicle verified successfully", mapToDto(saved));
    }

    private VehicleDto mapToDto(Vehicle vehicle) {
        return VehicleDto.builder()
                .id(vehicle.getId())
                .modelNumberId(vehicle.getModelNumber().getId())
                .registrationNo(vehicle.getRegistrationNo())
                .registrationFront(vehicle.getRegistrationFront())
                .registrationBack(vehicle.getRegistrationBack())
                .outdoorImages(vehicle.getOutdoorImages())
                .indoorImages(vehicle.getIndoorImages())
                .ac(vehicle.isAc())
                .petsAllowed(vehicle.isPetsAllowed())
                .smokingAllowed(vehicle.isSmokingAllowed())
                .vehicleVerified(vehicle.getVehicleVerified())
                .brandId(vehicle.getBrand().getId())
                .userId(vehicle.getUser().getId())
                .build();
    }
}
