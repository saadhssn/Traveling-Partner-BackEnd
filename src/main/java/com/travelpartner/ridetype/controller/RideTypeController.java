package com.travelpartner.ridetype.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.ridetype.dto.RideTypeDto;
import com.travelpartner.ridetype.service.RideTypeService;
import com.travelpartner.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rideTypes")
@RequiredArgsConstructor
public class RideTypeController {

    private final RideTypeService rideTypeService;

    @PostMapping("/create")
    @AuditAction(action = "CREATE", module = "RIDE_TYPE", description = "Create ride type")
    public ApiResponse<RideTypeDto> createRideType(@RequestBody RideTypeDto dto) {
        return rideTypeService.createRideType(dto);
    }

    @GetMapping("/getAll")
    @AuditAction(action = "READ", module = "RIDE_TYPE", description = "Get all ride types")
    public ApiResponse<List<RideTypeDto>> getAllRideTypes() {
        return rideTypeService.getAllRideTypes();
    }

    @GetMapping("/getById/{id}")
    @AuditAction(action = "READ", module = "RIDE_TYPE", description = "Get ride type by id")
    public ApiResponse<RideTypeDto> getRideTypeById(@PathVariable Long id) {
        return rideTypeService.getRideTypeById(id);
    }

    @PutMapping("/update/{id}")
    @AuditAction(action = "UPDATE", module = "RIDE_TYPE", description = "Update ride type")
    public ApiResponse<RideTypeDto> updateRideType(
            @PathVariable Long id,
            @RequestBody RideTypeDto dto
    ) {
        return rideTypeService.updateRideType(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    @AuditAction(action = "DELETE", module = "RIDE_TYPE", description = "Delete ride type")
    public ApiResponse<Void> deleteRideType(@PathVariable Long id) {
        return rideTypeService.deleteRideType(id);
    }
}