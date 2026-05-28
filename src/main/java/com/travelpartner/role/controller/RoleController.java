package com.travelpartner.role.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.role.dto.RoleDto;
import com.travelpartner.role.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    private final RoleService service;

    @PostMapping("/create")
    @AuditAction(action = "CREATE", module = "ROLE", description = "Create new role")
    public ApiResponse<RoleDto> create(@Valid @RequestBody RoleDto dto) {
        return service.create(dto);
    }

    @GetMapping("/getAll")
    @AuditAction(action = "READ", module = "ROLE", description = "Get all roles")
    public ApiResponse<Page<RoleDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.getAll(page, size);
    }

    @GetMapping("/getById/{id}")
    @AuditAction(action = "READ", module = "ROLE", description = "Get role by id")
    public ApiResponse<RoleDto> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/update/{id}")
    @AuditAction(action = "UPDATE", module = "ROLE", description = "Update role")
    public ApiResponse<RoleDto> update(
            @PathVariable Long id,
            @Valid @RequestBody RoleDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    @AuditAction(action = "DELETE", module = "ROLE", description = "Delete role")
    public ApiResponse<String> delete(@PathVariable Long id) {
        return service.delete(id);
    }
}