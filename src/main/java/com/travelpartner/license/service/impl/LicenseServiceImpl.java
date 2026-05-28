package com.travelpartner.license.service.impl;

import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.license.dto.LicenseDto;
import com.travelpartner.license.model.License;
import com.travelpartner.license.repository.LicenseRepository;
import com.travelpartner.license.service.LicenseService;
import com.travelpartner.user.enums.UserStatus;
import com.travelpartner.user.model.User;
import com.travelpartner.user.repository.UserRepository;
import com.travelpartner.user.util.RoleChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LicenseServiceImpl implements LicenseService {

    private final LicenseRepository repository;
    private final UserRepository userRepository;
    private final RoleChecker roleChecker;

    @Override
    public ApiResponse<LicenseDto> create(LicenseDto dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ===== DUPLICATE CHECK =====
        if (dto.getLicenseNo() != null &&
                repository.existsByLicenseNo(dto.getLicenseNo())) {
            throw new RuntimeException("License number already assigned to another user");
        }

        // prevent duplicate license per user
        if (repository.findByUser(user).isPresent()) {
            throw new RuntimeException("License already exists for this user");
        }

        License license = License.builder()
                .user(user)
                .licenseNo(dto.getLicenseNo())
                .licenseFront(dto.getLicenseFront())
                .licenseBack(dto.getLicenseBack())
                .licenseVerified(false)
                .build();

        License saved = repository.save(license);

        return ApiResponse.success("License created", mapToDto(saved));
    }

    @Override
    public ApiResponse<Page<LicenseDto>> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<LicenseDto> result = repository.findByIsDeletedFalse(pageable).map(this::mapToDto);
        return ApiResponse.success("License list", result);
    }

    @Override
    public ApiResponse<Page<LicenseDto>> getAllFiltered(int page, int size, Boolean verified) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<LicenseDto> result = repository.findByIsDeletedFalseAndVerified(verified, pageable)
                .map(this::mapToDto);
        return ApiResponse.success("License list filtered", result);
    }

    @Override
    public ApiResponse<LicenseDto> getById(Long id) {
        License license = repository.findById(id)
                .filter(l -> !l.getIsDeleted())
                .orElseThrow(() -> new RuntimeException("License not found"));
        return ApiResponse.success("License found", mapToDto(license));
    }

    @Override
    public ApiResponse<LicenseDto> update(Long id, LicenseDto dto) {

        License license = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("License not found"));

        // ===== DUPLICATE CHECK =====
        if (dto.getLicenseNo() != null &&
                repository.findByLicenseNoAndIdNot(dto.getLicenseNo(), id).isPresent()) {
            throw new RuntimeException("License number already assigned to another user");
        }

        if (dto.getLicenseNo() != null)
            license.setLicenseNo(dto.getLicenseNo());

        if (dto.getLicenseFront() != null)
            license.setLicenseFront(dto.getLicenseFront());

        if (dto.getLicenseBack() != null)
            license.setLicenseBack(dto.getLicenseBack());

        License saved = repository.save(license);

        return ApiResponse.success("License updated", mapToDto(saved));
    }

    @Override
    public ApiResponse<LicenseDto> verifyLicense(Long id, LicenseDto dto) {
        License license = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("License not found"));

        User currentUser = license.getUser();
        if (!roleChecker.isAdmin(currentUser)) {
            throw new RuntimeException("Only admin can verify license");
        }

        license.setLicenseVerified(true);
        license.getUser().setLicenseVerified(true);

        // Auto approve user if license + vehicle verified
        if (license.getUser().getVehicleVerified() != null &&
                license.getUser().getVehicleVerified() &&
                license.getUser().getLicenseVerified()) {

            license.getUser().setStatus(UserStatus.APPROVED);
        }

        return ApiResponse.success("License verified", mapToDto(repository.save(license)));
    }

    @Override
    public ApiResponse<Void> delete(Long id) {
        License license = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("License not found"));
        license.setIsDeleted(true);
        license.setDeletedAt(LocalDateTime.now());
        repository.save(license);
        return ApiResponse.success("License soft deleted", null);
    }

    private LicenseDto mapToDto(License entity) {
        return LicenseDto.builder()
                .userId(entity.getUser().getId())
                .licenseNo(entity.getLicenseNo())
                .licenseFront(entity.getLicenseFront())
                .licenseBack(entity.getLicenseBack())
                .licenseVerified(entity.getLicenseVerified())
                .build();
    }
}
