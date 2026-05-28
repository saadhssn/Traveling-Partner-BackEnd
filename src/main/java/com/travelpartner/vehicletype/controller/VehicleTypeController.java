package com.travelpartner.vehicletype.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.vehicletype.dto.VehicleTypeDto;
import com.travelpartner.vehicletype.service.VehicleTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicleTypes")
@RequiredArgsConstructor
public class VehicleTypeController {

    private final VehicleTypeService service;

    @PostMapping("/create")
    @AuditAction(
            action = "CREATE",
            module = "VEHICLE_TYPE",
            description = "Create vehicle type"
    )
    public ApiResponse<VehicleTypeDto> create(@RequestBody VehicleTypeDto dto) {
        return service.create(dto);
    }

    @GetMapping("/getAll")
    @AuditAction(
            action = "VIEW",
            module = "VEHICLE_TYPE",
            description = "View vehicle types with pagination and search"
    )
    public ApiResponse<?> getAllPaged(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.getAllWithPagination(search, page, size);
    }

    @GetMapping("/getById/{id}")
    @AuditAction(
            action = "VIEW",
            module = "VEHICLE_TYPE",
            description = "View vehicle type by id"
    )
    public ApiResponse<VehicleTypeDto> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/update/{id}")
    @AuditAction(
            action = "UPDATE",
            module = "VEHICLE_TYPE",
            description = "Update vehicle type"
    )
    public ApiResponse<VehicleTypeDto> update(@PathVariable Long id,
                                              @RequestBody VehicleTypeDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    @AuditAction(
            action = "DELETE",
            module = "VEHICLE_TYPE",
            description = "Delete vehicle type"
    )
    public ApiResponse<Void> delete(@PathVariable Long id) {
        return service.delete(id);
    }
}