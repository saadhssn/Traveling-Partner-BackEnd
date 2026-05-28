package com.travelpartner.role.service.impl;

import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.role.dto.RoleDto;
import com.travelpartner.role.model.Role;
import com.travelpartner.role.repository.RoleRepository;
import com.travelpartner.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    @Override
    public ApiResponse<RoleDto> create(RoleDto dto) {

        if (repository.existsByName(dto.getName()))
            throw new RuntimeException("Role name already exists");

        if (repository.existsBySlug(dto.getSlug()))
            throw new RuntimeException("Role slug already exists");

        Role role = Role.builder()
                .name(dto.getName().toUpperCase())
                .slug(dto.getSlug().toLowerCase())
                .build();

        return ApiResponse.success("Role created successfully",
                mapToDto(repository.save(role)));
    }

    @Override
    public ApiResponse<Page<RoleDto>> getAll(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<RoleDto> result = repository.findAll(pageable)
                .map(this::mapToDto);

        return ApiResponse.success("Roles fetched successfully", result);
    }

    @Override
    public ApiResponse<RoleDto> getById(Long id) {

        Role role = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        return ApiResponse.success("Role fetched successfully", mapToDto(role));
    }

    @Override
    public ApiResponse<RoleDto> update(Long id, RoleDto dto) {

        Role role = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        role.setName(dto.getName().toUpperCase());
        role.setSlug(dto.getSlug().toLowerCase());

        return ApiResponse.success("Role updated successfully",
                mapToDto(repository.save(role)));
    }

    @Override
    public ApiResponse<String> delete(Long id) {

        Role role = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        repository.delete(role);

        return ApiResponse.success("Role deleted successfully", null);
    }

    /* ================= MAPPER ================= */

    private RoleDto mapToDto(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .slug(role.getSlug())
                .build();
    }
}