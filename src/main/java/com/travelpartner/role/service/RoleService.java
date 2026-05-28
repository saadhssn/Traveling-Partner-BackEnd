package com.travelpartner.role.service;

import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.role.dto.RoleDto;
import org.springframework.data.domain.Page;

public interface RoleService {

    ApiResponse<RoleDto> create(RoleDto dto);

    ApiResponse<Page<RoleDto>> getAll(int page, int size);

    ApiResponse<RoleDto> getById(Long id);

    ApiResponse<RoleDto> update(Long id, RoleDto dto);

    ApiResponse<String> delete(Long id);
}