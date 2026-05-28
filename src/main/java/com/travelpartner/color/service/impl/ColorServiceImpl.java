package com.travelpartner.color.service.impl;

import com.travelpartner.color.dto.ColorDto;
import com.travelpartner.color.enums.ColorStatus;
import com.travelpartner.color.model.Color;
import com.travelpartner.color.repository.ColorRepository;
import com.travelpartner.color.service.ColorService;
import com.travelpartner.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {

    private final ColorRepository repository;

    @Override
    public ApiResponse<ColorDto> create(ColorDto dto) {

        Color color = Color.builder()
                .name(dto.getName())
                .image(dto.getImage())
                .status(dto.getStatus() != null
                        ? dto.getStatus()
                        : ColorStatus.ACTIVE)
                .build();

        return ApiResponse.success(
                "Color created",
                mapToDto(repository.save(color))
        );
    }

    @Override
    public ApiResponse<Page<ColorDto>> getAll(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<ColorDto> result =
                repository.findAll(pageable)
                        .map(this::mapToDto);

        return ApiResponse.success("Color list", result);
    }

    @Override
    public ApiResponse<Page<ColorDto>> getAllFiltered(
            int page,
            int size,
            String search) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<ColorDto> result;

        if (search != null && !search.isEmpty()) {

            result = repository.findByNameContainingIgnoreCase(search, pageable)
                    .map(this::mapToDto);

        } else {

            result = repository.findAll(pageable)
                    .map(this::mapToDto);
        }

        return ApiResponse.success("Filtered color list", result);
    }

    @Override
    public ApiResponse<ColorDto> getById(Long id) {

        Color color = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Color not found"));

        return ApiResponse.success("Color found", mapToDto(color));
    }

    @Override
    public ApiResponse<ColorDto> update(Long id, ColorDto dto) {

        Color color = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Color not found"));

        if (dto.getName() != null) {
            color.setName(dto.getName());
        }

        if (dto.getImage() != null) {
            color.setImage(dto.getImage());
        }

        if (dto.getStatus() != null) {
            color.setStatus(dto.getStatus());
        }

        return ApiResponse.success(
                "Color updated",
                mapToDto(repository.save(color))
        );
    }

    @Override
    public ApiResponse<Void> delete(Long id) {

        Color color = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Color not found"));

        repository.delete(color);

        return ApiResponse.success("Color deleted", null);
    }

    private ColorDto mapToDto(Color entity) {

        return ColorDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .image(entity.getImage())
                .status(entity.getStatus())
                .build();
    }
}