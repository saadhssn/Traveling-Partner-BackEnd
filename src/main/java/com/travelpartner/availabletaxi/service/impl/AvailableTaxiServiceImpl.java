package com.travelpartner.availabletaxi.service.impl;

import com.travelpartner.availabletaxi.dto.AvailableTaxiDto;
import com.travelpartner.availabletaxi.dto.AvailableTaxiFullDto;
import com.travelpartner.availabletaxi.enums.DriverStatus;
import com.travelpartner.availabletaxi.enums.Gender;
import com.travelpartner.availabletaxi.model.AvailableTaxi;
import com.travelpartner.availabletaxi.repository.AvailableTaxiRepository;
import com.travelpartner.availabletaxi.service.AvailableTaxiService;
import com.travelpartner.basicinformation.repository.BasicInformationRepository;
import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.license.dto.LicenseDto;
import com.travelpartner.user.model.User;
import com.travelpartner.user.repository.UserRepository;
import com.travelpartner.vehicle.dto.VehicleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvailableTaxiServiceImpl implements AvailableTaxiService {

    private final AvailableTaxiRepository availableTaxiRepository;
    private final UserRepository userRepository;
    private final BasicInformationRepository basicInformationRepository;

    private String getDriverName(Long userId) {
        return basicInformationRepository.findByUser_Id(userId)
                .map(info -> {
                    String first = info.getFirstName() != null ? info.getFirstName() : "";
                    String last = info.getLastName() != null ? info.getLastName() : "";
                    return (first + " " + last).trim();
                })
                .orElse(null);
    }

    // Convert Entity -> DTO
    private AvailableTaxiDto mapToDto(AvailableTaxi taxi) {
        return AvailableTaxiDto.builder()
                .id(taxi.getId())
                .driverId(taxi.getDriver().getId())
                .city(taxi.getCity())
                .gender(taxi.getGender())
                .driverStatus(taxi.getDriverStatus())
                .latitude(taxi.getLatitude())
                .longitude(taxi.getLongitude())
                .build();
    }

    // Convert DTO -> Entity
    private AvailableTaxi mapToEntity(AvailableTaxiDto dto) {
        User driver = userRepository.findById(dto.getDriverId())
                .orElseThrow(() -> new IllegalArgumentException("Driver not found"));

        boolean isDriver = driver.getRoles().stream()
                .anyMatch(role -> role.getSlug().equalsIgnoreCase("DRIVER"));
        if (!isDriver) {
            throw new IllegalArgumentException("User must have DRIVER role");
        }

        return AvailableTaxi.builder()
                .driver(driver)
                .city(dto.getCity())
                .gender(dto.getGender())
                .driverStatus(dto.getDriverStatus())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
    }

    @Override
    public ApiResponse<AvailableTaxiDto> createAvailableTaxi(AvailableTaxiDto dto) {

        User driver = userRepository.findById(dto.getDriverId())
                .orElseThrow(() -> new IllegalArgumentException("Driver not found"));

        boolean isDriver = driver.getRoles().stream()
                .anyMatch(role -> role.getSlug().equalsIgnoreCase("DRIVER"));

        if (!isDriver) {
            throw new IllegalArgumentException("User must have DRIVER role");
        }

        AvailableTaxi taxi = availableTaxiRepository
                .findByDriver_Id(dto.getDriverId())
                .orElse(null);

        // CREATE
        if (taxi == null) {

            taxi = AvailableTaxi.builder()
                    .driver(driver)
                    .city(dto.getCity())
                    .gender(dto.getGender())
                    .driverStatus(dto.getDriverStatus())
                    .latitude(dto.getLatitude())
                    .longitude(dto.getLongitude())
                    .build();

            AvailableTaxi saved = availableTaxiRepository.save(taxi);

            return ApiResponse.success(
                    "AvailableTaxi created successfully",
                    mapToDto(saved)
            );
        }

        // UPDATE
        if (dto.getCity() != null) {
            taxi.setCity(dto.getCity());
        }

        if (dto.getGender() != null) {
            taxi.setGender(dto.getGender());
        }

        if (dto.getDriverStatus() != null) {

            taxi.setDriverStatus(dto.getDriverStatus());

            if (dto.getDriverStatus() == DriverStatus.ONLINE) {

                taxi.setOnlineSince(LocalDateTime.now());

                taxi.setAwaitingConfirmation(false);

                taxi.setConfirmationSentAt(null);
            }

            if (dto.getDriverStatus() == DriverStatus.OFFLINE) {

                taxi.setOnlineSince(null);

                taxi.setAwaitingConfirmation(false);

                taxi.setConfirmationSentAt(null);
            }
        }

        if (dto.getLatitude() != null) {
            taxi.setLatitude(dto.getLatitude());
        }

        if (dto.getLongitude() != null) {
            taxi.setLongitude(dto.getLongitude());
        }

        AvailableTaxi updated = availableTaxiRepository.save(taxi);

        return ApiResponse.success(
                "AvailableTaxi updated successfully",
                mapToDto(updated)
        );
    }

    @Override
    public ApiResponse<List<AvailableTaxiDto>> getAllAvailableTaxis() {
        List<AvailableTaxiDto> dtos = availableTaxiRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ApiResponse.success("AvailableTaxis fetched successfully", dtos);
    }

//    @Override
//    public ApiResponse<AvailableTaxiDto> getAvailableTaxiById(Long id) {
//        return availableTaxiRepository.findById(id)
//                .map(taxi -> ApiResponse.success("AvailableTaxi fetched successfully", mapToDto(taxi)))
//                .orElse(ApiResponse.error("AvailableTaxi not found", null));
//    }

    @Override
    public ApiResponse<AvailableTaxiDto> updateDriverStatus(Long id, AvailableTaxiDto dto) {

        AvailableTaxi taxi = availableTaxiRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("AvailableTaxi not found"));

        taxi.setCity(dto.getCity());
        taxi.setGender(dto.getGender());
        taxi.setDriverStatus(dto.getDriverStatus());
        taxi.setLatitude(dto.getLatitude());
        taxi.setLongitude(dto.getLongitude());

        AvailableTaxi updated = availableTaxiRepository.save(taxi);

        return ApiResponse.success("Driver status updated successfully", mapToDto(updated));
    }

    @Override
    public ApiResponse<Void> deleteAvailableTaxi(Long id) {
        AvailableTaxi taxi = availableTaxiRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("AvailableTaxi not found"));

        availableTaxiRepository.delete(taxi);
        return ApiResponse.success("AvailableTaxi deleted successfully", null);
    }

    @Override
    public ApiResponse<Page<AvailableTaxiFullDto>> getAvailableTaxisByGenderAndCity(
            Gender gender,
            String city,
            int page,
            int size) {

        // Convert empty strings → null (IMPORTANT FIX)
        if (city == null || city.trim().isEmpty()) {
            city = null;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<AvailableTaxi> taxis = availableTaxiRepository.findOnlineDrivers(
                gender,
                city,
                pageable
        );

        Page<AvailableTaxiFullDto> result = taxis.map(taxi -> {

            VehicleDto vehicleDto = null;
            LicenseDto licenseDto = null;

            if (taxi.getDriver().getVehicle() != null) {
                vehicleDto = VehicleDto.builder()
                        .id(taxi.getDriver().getVehicle().getId())
                        .modelNumberId(taxi.getDriver().getVehicle().getModelNumber().getId())
                        .registrationNo(taxi.getDriver().getVehicle().getRegistrationNo())
                        .registrationFront(taxi.getDriver().getVehicle().getRegistrationFront())
                        .registrationBack(taxi.getDriver().getVehicle().getRegistrationBack())
                        .outdoorImages(taxi.getDriver().getVehicle().getOutdoorImages())
                        .indoorImages(taxi.getDriver().getVehicle().getIndoorImages())
                        .ac(taxi.getDriver().getVehicle().isAc())
                        .petsAllowed(taxi.getDriver().getVehicle().isPetsAllowed())
                        .smokingAllowed(taxi.getDriver().getVehicle().isSmokingAllowed())
                        .vehicleVerified(taxi.getDriver().getVehicle().getVehicleVerified())
                        .brandId(taxi.getDriver().getVehicle().getBrand().getId())
                        .userId(taxi.getDriver().getId())
                        .build();
            }

            if (taxi.getDriver().getLicense() != null) {
                licenseDto = LicenseDto.builder()
                        .userId(taxi.getDriver().getId())
                        .licenseNo(taxi.getDriver().getLicense().getLicenseNo())
                        .licenseFront(taxi.getDriver().getLicense().getLicenseFront())
                        .licenseBack(taxi.getDriver().getLicense().getLicenseBack())
                        .licenseVerified(taxi.getDriver().getLicense().getLicenseVerified())
                        .build();
            }

            return AvailableTaxiFullDto.builder()
                    .id(taxi.getId())
                    .driverId(taxi.getDriver().getId())
                    .driverName(getDriverName(taxi.getDriver().getId()))
                    .city(taxi.getCity())
                    .gender(taxi.getGender())
                    .driverStatus(taxi.getDriverStatus())
                    .latitude(taxi.getLatitude())
                    .longitude(taxi.getLongitude())
                    .vehicle(vehicleDto)
                    .license(licenseDto)
                    .build();
        });

        return ApiResponse.success("Filtered AvailableTaxis fetched successfully", result);
    }

    @Override
    public ApiResponse<AvailableTaxiFullDto> getAvailableTaxiById(Long id) {
        AvailableTaxi taxi = availableTaxiRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("AvailableTaxi not found"));

        VehicleDto vehicleDto = null;
        LicenseDto licenseDto = null;

        if (taxi.getDriver().getVehicle() != null) {
            vehicleDto = VehicleDto.builder()
                    .id(taxi.getDriver().getVehicle().getId())
                    .modelNumberId(taxi.getDriver().getVehicle().getModelNumber().getId())
                    .registrationNo(taxi.getDriver().getVehicle().getRegistrationNo())
                    .registrationFront(taxi.getDriver().getVehicle().getRegistrationFront())
                    .registrationBack(taxi.getDriver().getVehicle().getRegistrationBack())
                    .outdoorImages(taxi.getDriver().getVehicle().getOutdoorImages())
                    .indoorImages(taxi.getDriver().getVehicle().getIndoorImages())
                    .ac(taxi.getDriver().getVehicle().isAc())
                    .petsAllowed(taxi.getDriver().getVehicle().isPetsAllowed())
                    .smokingAllowed(taxi.getDriver().getVehicle().isSmokingAllowed())
                    .vehicleVerified(taxi.getDriver().getVehicle().getVehicleVerified())
                    .brandId(taxi.getDriver().getVehicle().getBrand().getId())
                    .userId(taxi.getDriver().getId())
                    .build();
        }

        if (taxi.getDriver().getLicense() != null) {
            licenseDto = LicenseDto.builder()
                    .userId(taxi.getDriver().getId())
                    .licenseNo(taxi.getDriver().getLicense().getLicenseNo())
                    .licenseFront(taxi.getDriver().getLicense().getLicenseFront())
                    .licenseBack(taxi.getDriver().getLicense().getLicenseBack())
                    .licenseVerified(taxi.getDriver().getLicense().getLicenseVerified())
                    .build();
        }

        AvailableTaxiFullDto dto = AvailableTaxiFullDto.builder()
                .id(taxi.getId())
                .driverId(taxi.getDriver().getId())
                .driverName(getDriverName(taxi.getDriver().getId()))
                .city(taxi.getCity())
                .gender(taxi.getGender())
                .driverStatus(taxi.getDriverStatus())
                .latitude(taxi.getLatitude())
                .longitude(taxi.getLongitude())
                .vehicle(vehicleDto)
                .license(licenseDto)
                .build();

        return ApiResponse.success("AvailableTaxi fetched successfully", dto);
    }

    @Override
    public ApiResponse<String> confirmOnline(
            Long driverId,
            boolean online
    ) {

        AvailableTaxi taxi =
                availableTaxiRepository
                        .findByDriver_Id(driverId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Driver not found"
                                ));

        if (online) {

            taxi.setDriverStatus(DriverStatus.ONLINE);

            taxi.setOnlineSince(LocalDateTime.now());

        } else {

            taxi.setDriverStatus(DriverStatus.OFFLINE);

            taxi.setOnlineSince(null);
        }

        taxi.setAwaitingConfirmation(false);

        taxi.setConfirmationSentAt(null);

        availableTaxiRepository.save(taxi);

        return ApiResponse.success(
                "Driver status updated",
                online ? "ONLINE" : "OFFLINE"
        );
    }
}