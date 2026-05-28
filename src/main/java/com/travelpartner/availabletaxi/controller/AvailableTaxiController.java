package com.travelpartner.availabletaxi.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.availabletaxi.dto.AvailableTaxiDto;
import com.travelpartner.availabletaxi.dto.AvailableTaxiFullDto;
import com.travelpartner.availabletaxi.dto.AvailableTaxiStatusDto;
import com.travelpartner.availabletaxi.enums.Gender;
import com.travelpartner.availabletaxi.service.AvailableTaxiService;
import com.travelpartner.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availableTaxis")
@RequiredArgsConstructor
public class AvailableTaxiController {

    private final AvailableTaxiService availableTaxiService;

    @PostMapping("/create")
    @AuditAction(action = "CREATE", module = "AVAILABLE_TAXI", description = "Create available taxi")
    public ApiResponse<AvailableTaxiDto> createAvailableTaxi(@RequestBody AvailableTaxiDto dto) {
        return availableTaxiService.createAvailableTaxi(dto);
    }

    @GetMapping("/getAll")
    @AuditAction(action = "READ", module = "AVAILABLE_TAXI", description = "Get all available taxis")
    public ApiResponse<List<AvailableTaxiDto>> getAllAvailableTaxis() {
        return availableTaxiService.getAllAvailableTaxis();
    }


    @GetMapping("/getAllOnline")
    @AuditAction(action = "READ", module = "AVAILABLE_TAXI", description = "Get available taxis by gender and city")
    public ApiResponse<Page<AvailableTaxiFullDto>> getAllOnline(
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return availableTaxiService.getAvailableTaxisByGenderAndCity(gender, city, page, size);
    }

    @GetMapping("/getById/{id}")
    @AuditAction(action = "READ", module = "AVAILABLE_TAXI", description = "Get available taxi by id")
    public ApiResponse<AvailableTaxiFullDto> getAvailableTaxiById(@PathVariable Long id) {
        return availableTaxiService.getAvailableTaxiById(id);
    }

    @PutMapping("/update/{id}/status")
    @AuditAction(action = "UPDATE", module = "AVAILABLE_TAXI", description = "Update driver status")
    public ApiResponse<AvailableTaxiDto> updateDriverStatus(
            @PathVariable Long id,
            @RequestBody AvailableTaxiDto dto
    ) {
        return availableTaxiService.updateDriverStatus(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    @AuditAction(action = "DELETE", module = "AVAILABLE_TAXI", description = "Delete available taxi")
    public ApiResponse<Void> deleteAvailableTaxi(@PathVariable Long id) {
        return availableTaxiService.deleteAvailableTaxi(id);
    }

    @PostMapping("/{driverId}/confirm-online")
    public ApiResponse<String> confirmOnline(
            @PathVariable Long driverId,
            @RequestParam boolean online
    ) {

        return availableTaxiService.confirmOnline(driverId, online);
    }
}