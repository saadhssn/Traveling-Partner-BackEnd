package com.travelpartner.basicinformation.service.impl;

import com.travelpartner.basicinformation.dto.BasicInformationDto;
import com.travelpartner.basicinformation.model.BasicInformation;
import com.travelpartner.basicinformation.repository.BasicInformationRepository;
import com.travelpartner.basicinformation.service.BasicInformationService;
import com.travelpartner.common.exception.BadRequestException;
import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.user.model.User;
import com.travelpartner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BasicInformationServiceImpl implements BasicInformationService {

    private final BasicInformationRepository repository;
    private final UserRepository userRepository;

    @Override
    public ApiResponse<BasicInformationDto> create(BasicInformationDto dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new BadRequestException("User not found"));

        // Prevent multiple basic info for same user
        if (repository.findByUser(user).isPresent()) {
            throw new BadRequestException("Basic information already exists for this user");
        }

        // ===== DUPLICATE CHECKS =====

        if (dto.getEmail() != null && repository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email already assigned to another user");
        }

        if (dto.getWhatsApp() != null && repository.existsByWhatsApp(dto.getWhatsApp())) {
            throw new BadRequestException("WhatsApp number already assigned");
        }

        if (dto.getCnicNumber() != null && repository.existsByCnicNumber(dto.getCnicNumber())) {
            throw new BadRequestException("CNIC already assigned to another user");
        }

        // Build AFTER validation
        BasicInformation basic = BasicInformation.builder()
                .user(user)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .gender(dto.getGender())
                .whatsApp(dto.getWhatsApp())
                .email(dto.getEmail())
                .cnicNumber(dto.getCnicNumber())
                .cnicFront(dto.getCnicFront())
                .cnicBack(dto.getCnicBack())
                .profilePicture(dto.getProfilePicture())
                .referralCode(dto.getReferralCode())
                .acceptTerm(dto.getAcceptTerm())
                .city(dto.getCity())
                .build();

        BasicInformation saved = repository.save(basic);

        return ApiResponse.success("Basic information created", mapToDto(saved));
    }

    @Override
    public ApiResponse<Page<BasicInformationDto>> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BasicInformationDto> result = repository.findAll(pageable)
                .map(this::mapToDto);
        return ApiResponse.success("Basic information list", result);
    }

    @Override
    public ApiResponse<Page<BasicInformationDto>> getAllFiltered(int page, int size, Boolean deleted) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BasicInformationDto> result = repository.findByIsDeleted(deleted, pageable)
                .map(this::mapToDto);
        return ApiResponse.success("Basic information list filtered", result);
    }

    @Override
    public ApiResponse<BasicInformationDto> getById(Long id) {
        BasicInformation basic = repository.findById(id)
                .filter(b -> !b.getIsDeleted())
                .orElseThrow(() -> new BadRequestException("Basic information not found"));
        return ApiResponse.success("Basic information found", mapToDto(basic));
    }

    @Override
    public ApiResponse<BasicInformationDto> update(Long id, BasicInformationDto dto) {

        BasicInformation basic = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Basic information not found"));

        if (Boolean.TRUE.equals(basic.getIsDeleted())) {
            throw new BadRequestException("Cannot update deleted basic information");
        }

        // ===== DUPLICATE CHECKS =====

        if (dto.getEmail() != null &&
                repository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new BadRequestException("Email already assigned to another user");
        }

        if (dto.getWhatsApp() != null &&
                repository.existsByWhatsAppAndIdNot(dto.getWhatsApp(), id)) {
            throw new BadRequestException("WhatsApp number already assigned");
        }

        if (dto.getCnicNumber() != null &&
                repository.existsByCnicNumberAndIdNot(dto.getCnicNumber(), id)) {
            throw new BadRequestException("CNIC already assigned to another user");
        }

        // Update only if not null (better practice)
        if (dto.getFirstName() != null)
            basic.setFirstName(dto.getFirstName());

        if (dto.getLastName() != null)
            basic.setLastName(dto.getLastName());

        if (dto.getGender() != null)
            basic.setGender(dto.getGender());

        if (dto.getWhatsApp() != null)
            basic.setWhatsApp(dto.getWhatsApp());

        if (dto.getEmail() != null)
            basic.setEmail(dto.getEmail());

        if (dto.getCnicNumber() != null)
            basic.setCnicNumber(dto.getCnicNumber());

        if (dto.getCnicFront() != null)
            basic.setCnicFront(dto.getCnicFront());

        if (dto.getCnicBack() != null)
            basic.setCnicBack(dto.getCnicBack());

        if (dto.getProfilePicture() != null)
            basic.setProfilePicture(dto.getProfilePicture());

        if (dto.getReferralCode() != null)
            basic.setReferralCode(dto.getReferralCode());

        if (dto.getAcceptTerm() != null)
            basic.setAcceptTerm(dto.getAcceptTerm());

        if (dto.getCity() != null)
            basic.setCity(dto.getCity());

        BasicInformation updated = repository.save(basic);

        return ApiResponse.success("Basic information updated", mapToDto(updated));
    }

    @Override
    public ApiResponse<Void> delete(Long id) {
        BasicInformation basic = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Basic information not found"));

        basic.setIsDeleted(true);
        basic.setDeletedAt(LocalDateTime.now());
        repository.save(basic);

        return ApiResponse.success("Basic information soft deleted", null);
    }

    private BasicInformationDto mapToDto(BasicInformation entity) {
        return BasicInformationDto.builder()
                .userId(entity.getUser().getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .gender(entity.getGender())
                .whatsApp(entity.getWhatsApp())
                .email(entity.getEmail())
                .cnicNumber(entity.getCnicNumber())
                .cnicFront(entity.getCnicFront())
                .cnicBack(entity.getCnicBack())
                .profilePicture(entity.getProfilePicture())
                .referralCode(entity.getReferralCode())
                .acceptTerm(entity.getAcceptTerm())
                .city(entity.getCity())
                .build();
    }
}
