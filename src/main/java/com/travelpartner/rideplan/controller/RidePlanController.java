package com.travelpartner.rideplan.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.rideplan.dto.RidePlanDto;
import com.travelpartner.rideplan.dto.RideStatusUpdateDto;
import com.travelpartner.rideplan.enums.RideStatus;
import com.travelpartner.rideplan.service.RidePlanService;
import com.travelpartner.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ridePlans")
@RequiredArgsConstructor
public class RidePlanController {

    private final RidePlanService ridePlanService;

    @PostMapping("/create")
    @AuditAction(action = "CREATE", module = "RIDE_PLAN", description = "Create ride plan")
    public ApiResponse<RidePlanDto> createRidePlan(@RequestBody RidePlanDto dto) {
        return ridePlanService.createRidePlan(dto);
    }

    @GetMapping("/getAll")
    @AuditAction(action = "READ", module = "RIDE_PLAN", description = "Get all ride plans")
    public ApiResponse<Page<RidePlanDto>> getAllRidePlans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ridePlanService.getAllRidePlans(page, size);
    }

    @GetMapping("/getById/{id}")
    @AuditAction(action = "READ", module = "RIDE_PLAN", description = "Get ride plan by id")
    public ApiResponse<RidePlanDto> getRidePlanById(@PathVariable Long id) {
        return ridePlanService.getRidePlanById(id);
    }

    @PutMapping("/update/{id}")
    @AuditAction(action = "UPDATE", module = "RIDE_PLAN", description = "Update ride plan")
    public ApiResponse<RidePlanDto> updateRidePlan(@PathVariable Long id, @RequestBody RidePlanDto dto) {
        return ridePlanService.updateRidePlan(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    @AuditAction(action = "DELETE", module = "RIDE_PLAN", description = "Delete ride plan")
    public ApiResponse<Void> deleteRidePlan(@PathVariable Long id) {
        return ridePlanService.deleteRidePlan(id);
    }

    @PatchMapping("/updateStatus/{id}")
    @AuditAction(action = "UPDATE", module = "RIDE_PLAN", description = "Update ride status")
    public ApiResponse<RidePlanDto> updateRideStatus(
            @PathVariable Long id,
            @RequestBody RideStatusUpdateDto request) {

        return ridePlanService.updateRideStatus(
                id,
                request.getRideStatus(),
                request.getDriverId()
        );
    }

    @GetMapping("/driver/{driverId}")
    @AuditAction(
            action = "READ",
            module = "RIDE_PLAN",
            description = "Get ride plans by driver and status"
    )
    public ApiResponse<Page<RidePlanDto>> getRidePlansByDriverAndStatus(

            @PathVariable Long driverId,

            @RequestParam(required = false) RideStatus status,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size
    ) {

        return ridePlanService.getRidePlansByDriverAndStatus(
                driverId,
                status,
                page,
                size
        );
    }

    @GetMapping("/driver/{driverId}/upcoming")
    @AuditAction(
            action = "READ",
            module = "RIDE_PLAN",
            description = "Get upcoming accepted ride"
    )
    public ApiResponse<RidePlanDto> getUpcomingRide(
            @PathVariable Long driverId
    ) {

        return ridePlanService.getUpcomingRide(driverId);
    }

    @GetMapping("/user/{userId}")
    @AuditAction(
            action = "READ",
            module = "RIDE_PLAN",
            description = "Get ride plans by user type and status"
    )
    public ApiResponse<Page<RidePlanDto>> getRidePlansByUserTypeAndStatus(

            @PathVariable Long userId,

            @RequestParam String type,

            @RequestParam(required = false) RideStatus status,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size
    ) {

        return ridePlanService.getRidePlansByUserTypeAndStatus(
                userId,
                type,
                status,
                page,
                size
        );
    }
}