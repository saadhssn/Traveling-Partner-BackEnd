package com.travelpartner.vehicle.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.vehicle.dto.VehicleDto;
import com.travelpartner.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService service;

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    @AuditAction(
            action = "CREATE",
            module = "VEHICLE",
            description = "Admin created vehicle"
    )
    public ApiResponse<VehicleDto> create(@Valid @RequestBody VehicleDto dto) {
        return service.create(dto);
    }

    @GetMapping("/getAll")
    @AuditAction(
            action = "VIEW",
            module = "VEHICLE",
            description = "View all vehicles"
    )
    public ApiResponse<Page<VehicleDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.getAll(page, size);
    }

    @GetMapping("/getById/{id}")
    @AuditAction(
            action = "VIEW",
            module = "VEHICLE",
            description = "View vehicle by id"
    )
    public ApiResponse<VehicleDto> getById(@PathVariable Long id) {
        return service.getById(id);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    @AuditAction(
            action = "UPDATE",
            module = "VEHICLE",
            description = "Admin updated vehicle"
    )
    public ApiResponse<VehicleDto> update(@PathVariable Long id, @Valid @RequestBody VehicleDto dto) {
        return service.update(id, dto);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    @AuditAction(
            action = "DELETE",
            module = "VEHICLE",
            description = "Admin deleted vehicle"
    )
    public ApiResponse<Void> delete(@PathVariable Long id) {
        return service.delete(id);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/verify/{id}")
    @AuditAction(
            action = "VERIFY",
            module = "VEHICLE",
            description = "Admin verified vehicle"
    )
    public ApiResponse<VehicleDto> verify(@PathVariable Long id) {
        return service.verifyVehicle(id);
    }
}