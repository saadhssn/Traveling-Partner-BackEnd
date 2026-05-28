package com.travelpartner.modelnumber.service.impl;

import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.modelnumber.dto.ModelNumberDto;
import com.travelpartner.modelnumber.enums.ModelNumberStatus;
import com.travelpartner.modelnumber.model.ModelNumber;
import com.travelpartner.modelnumber.repository.ModelNumberRepository;
import com.travelpartner.modelnumber.service.ModelNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModelNumberServiceImpl implements ModelNumberService {

    private final ModelNumberRepository repository;

    @Override
    public ApiResponse<ModelNumberDto> create(ModelNumberDto dto) {

        ModelNumber entity = ModelNumber.builder()
                .name(dto.getName())
                .image(dto.getImage())
                .status(dto.getStatus() != null
                        ? dto.getStatus()
                        : ModelNumberStatus.ACTIVE)
                .build();

        return ApiResponse.success(
                "Model number created",
                mapToDto(repository.save(entity))
        );
    }

    @Override
    public ApiResponse<Page<ModelNumberDto>> getAll(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<ModelNumberDto> result =
                repository.findAll(pageable)
                        .map(this::mapToDto);

        return ApiResponse.success("Model number list", result);
    }

    @Override
    public ApiResponse<Page<ModelNumberDto>> getAllFiltered(
            int page,
            int size,
            String search) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<ModelNumber> result;

        if (search != null && !search.isEmpty()) {
            result = repository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            result = repository.findAll(pageable);
        }

        return ApiResponse.success(
                "Filtered model number list",
                result.map(this::mapToDto)
        );
    }

    @Override
    public ApiResponse<ModelNumberDto> getById(Long id) {

        ModelNumber entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ModelNumber not found"));

        return ApiResponse.success("Model number found", mapToDto(entity));
    }

    @Override
    public ApiResponse<ModelNumberDto> update(Long id, ModelNumberDto dto) {

        ModelNumber entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ModelNumber not found"));

        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }

        if (dto.getImage() != null) {
            entity.setImage(dto.getImage());
        }

        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }

        return ApiResponse.success(
                "Model number updated",
                mapToDto(repository.save(entity))
        );
    }

    @Override
    public ApiResponse<Void> delete(Long id) {

        ModelNumber entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ModelNumber not found"));

        repository.delete(entity);

        return ApiResponse.success("Model number deleted", null);
    }

    private ModelNumberDto mapToDto(ModelNumber entity) {

        return ModelNumberDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .image(entity.getImage())
                .status(entity.getStatus())
                .build();
    }
}