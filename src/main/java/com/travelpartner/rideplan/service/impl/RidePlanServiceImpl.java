package com.travelpartner.rideplan.service.impl;

import com.travelpartner.availabletaxi.enums.DriverStatus;
import com.travelpartner.availabletaxi.model.AvailableTaxi;
import com.travelpartner.availabletaxi.repository.AvailableTaxiRepository;
import com.travelpartner.chat.websocket.WebSocketSessionManager;
import com.travelpartner.notification.service.FirebasePushNotificationService;
import com.travelpartner.notification.service.RealtimeNotificationService;
import com.travelpartner.rideplan.dto.RidePlanDto;
import com.travelpartner.rideplan.dto.RideStatusUpdateDto;
import com.travelpartner.rideplan.enums.RideStatus;
import com.travelpartner.rideplan.model.RidePlan;
import com.travelpartner.rideplan.processor.RidePlanProcessor;
import com.travelpartner.rideplan.processor.RidePlanProcessorFactory;
import com.travelpartner.rideplan.repository.RidePlanRepository;
import com.travelpartner.rideplan.service.GoogleMapService;
import com.travelpartner.rideplan.service.RidePlanService;
import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.rideplan.websocket.RideRealtimeService;
import com.travelpartner.ridetype.repository.RideTypeRepository;
import com.travelpartner.role.repository.RoleRepository;
import com.travelpartner.user.model.User;
import com.travelpartner.user.repository.UserRepository;
import com.travelpartner.vehicle.dto.VehicleDto;
import com.travelpartner.vehicle.model.Vehicle;
import com.travelpartner.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RidePlanServiceImpl implements RidePlanService {

    private final RidePlanRepository ridePlanRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RideTypeRepository rideTypeRepository;
    private final AvailableTaxiRepository availableTaxiRepository;
    private final RealtimeNotificationService notificationService;
    private final RideRealtimeService rideRealtimeService;
    private final GoogleMapService googleMapService;
    private final VehicleRepository vehicleRepository;
    private final RidePlanProcessorFactory processorFactory;
    private final FirebasePushNotificationService firebasePushNotificationService;

    private RidePlanDto mapToDto(RidePlan ridePlan) {

        String userName = null;
        String driverName = null;

        VehicleDto vehicleDto = null;

        if (ridePlan.getDriver() != null) {

            vehicleDto = vehicleRepository
                    .findByUser_Id(ridePlan.getDriver().getId())
                    .map(this::mapVehicleToDto)
                    .orElse(null);
        }

        if (ridePlan.getUser() != null) {
            var userInfo = ridePlan.getUser().getBasicInformation();

            if (userInfo != null) {
                userName = buildFullName(userInfo.getFirstName(), userInfo.getLastName());
            }
        }

        if (ridePlan.getDriver() != null) {
            var driverInfo = ridePlan.getDriver().getBasicInformation();

            if (driverInfo != null) {
                driverName = buildFullName(driverInfo.getFirstName(), driverInfo.getLastName());
            }
        }

        return RidePlanDto.builder()
                .id(ridePlan.getId())

                .userId(ridePlan.getUser() != null ? ridePlan.getUser().getId() : null)
                .driverId(ridePlan.getDriver() != null ? ridePlan.getDriver().getId() : null)

                .userName(userName)
                .driverName(driverName)

                .role(ridePlan.getRole() != null ? ridePlan.getRole().getName() : null)
                .rideType(ridePlan.getRideType() != null ? ridePlan.getRideType().getName() : null)

                .date(ridePlan.getDate())
                .address(ridePlan.getAddress())
                .city(ridePlan.getCity())
                .stuff(ridePlan.getStuff())
                .bagsCount(ridePlan.getBagsCount())
                .pickUpLocation(ridePlan.getPickUpLocation())
                .dropOffLocation(ridePlan.getDropOffLocation())
                .tourDays(ridePlan.getTourDays())
                .meal(ridePlan.getMeal())
                .seats(ridePlan.getSeats())
                .seatsAvailable(ridePlan.getSeatsAvailable())
                .seatsReserved(ridePlan.getSeatsReserved())
                .visitingPoints(ridePlan.getVisitingPoints())
                .pets(ridePlan.getPets())
                .smoke(ridePlan.getSmoke())
                .ac(ridePlan.getAc())
                .fare(ridePlan.getFare())
                .driverQuotedFare(ridePlan.getDriverQuotedFare())
                .totalPassenger(ridePlan.getTotalPassenger())
                .partnerQuotedFare(ridePlan.getPartnerQuotedFare())
                .rideStatus(ridePlan.getRideStatus())
                .female(ridePlan.getFemale())
                .time(ridePlan.getTime())
                .note(ridePlan.getNote())
                .vehicle(vehicleDto)
                .pickupLatitude(ridePlan.getPickupLatitude())
                .pickupLongitude(ridePlan.getPickupLongitude())

                .dropoffLatitude(ridePlan.getDropoffLatitude())
                .dropoffLongitude(ridePlan.getDropoffLongitude())

                .estimatedDistance(ridePlan.getEstimatedDistance())
                .estimatedDuration(ridePlan.getEstimatedDuration())
                .estimatedDurationInSeconds(
                        ridePlan.getEstimatedDurationInSeconds()
                )
                .build();
    }

    private String buildFullName(String firstName, String lastName) {
        if (firstName == null && lastName == null) return null;
        if (firstName == null) return lastName;
        if (lastName == null) return firstName;
        return firstName + " " + lastName;
    }

    private RidePlan mapToEntity(RidePlanDto dto) {
        return RidePlan.builder()
                .user(dto.getUserId() != null ? userRepository.findById(dto.getUserId()).orElse(null) : null)
                .driver(dto.getDriverId() != null ? userRepository.findById(dto.getDriverId()).orElse(null) : null)
                .role(dto.getRole() != null
                        ? roleRepository.findByName(dto.getRole()).orElse(null)
                        : null)
                .rideType(dto.getRideType() != null
                        ? rideTypeRepository.findByName(dto.getRideType()).orElse(null)
                        : null)
                .date(dto.getDate())
                .address(dto.getAddress())
                .city(dto.getCity())
                .stuff(dto.getStuff())
                .bagsCount(dto.getBagsCount())
                .pickUpLocation(dto.getPickUpLocation())
                .dropOffLocation(dto.getDropOffLocation())
                .tourDays(dto.getTourDays())
                .meal(dto.getMeal())
                .seats(dto.getSeats())
                .seatsAvailable(dto.getSeatsAvailable())
                .seatsReserved(dto.getSeatsReserved())
                .visitingPoints(dto.getVisitingPoints())
                .pets(dto.getPets())
                .smoke(dto.getSmoke())
                .ac(dto.getAc())
                .fare(dto.getFare())
                .driverQuotedFare(dto.getDriverQuotedFare())
                .totalPassenger(dto.getTotalPassenger())
                .partnerQuotedFare(dto.getPartnerQuotedFare())
                .rideStatus(dto.getRideStatus())
                .female(dto.getFemale())
                .time(dto.getTime())
                .note(dto.getNote())
                .pickupLatitude(dto.getPickupLatitude())
                .pickupLongitude(dto.getPickupLongitude())

                .dropoffLatitude(dto.getDropoffLatitude())
                .dropoffLongitude(dto.getDropoffLongitude())

                .estimatedDistance(dto.getEstimatedDistance())
                .estimatedDuration(dto.getEstimatedDuration())
                .estimatedDurationInSeconds(dto.getEstimatedDurationInSeconds())
                .build();
    }

    @Override
    public ApiResponse<RidePlanDto> createRidePlan(RidePlanDto dto) {

        if (dto.getUserId() != null && !userRepository.existsById(dto.getUserId())) {
            return ApiResponse.error(404, "User not found");
        }

        if (dto.getDriverId() != null && !userRepository.existsById(dto.getDriverId())) {
            return ApiResponse.error(404, "Driver not found");
        }

        if (dto.getRole() != null && !roleRepository.existsByName(dto.getRole())) {
            return ApiResponse.error(404, "Role not found");
        }

        if (dto.getRideType() != null && !rideTypeRepository.existsByName(dto.getRideType())) {
            return ApiResponse.error(404, "RideType not found");
        }

        RidePlan ridePlan = mapToEntity(dto);

        if (ridePlan.getRideType() == null) {
            return ApiResponse.error(400, "Ride type is required");
        }

        String module = ridePlan.getRideType().getModule().name();

        RidePlanProcessor processor =
                processorFactory.getProcessor(module);

        processor.validate(dto);

        processor.process(dto, ridePlan);

        if (dto.getPickupLatitude() != null &&
                dto.getPickupLongitude() != null &&
                dto.getDropoffLatitude() != null &&
                dto.getDropoffLongitude() != null) {

            var matrix = googleMapService.getDistanceAndTime(
                    dto.getPickupLatitude(),
                    dto.getPickupLongitude(),
                    dto.getDropoffLatitude(),
                    dto.getDropoffLongitude()
            );

            if (matrix != null && "OK".equals(matrix.getStatus())) {

                ridePlan.setEstimatedDistance(
                        matrix.getDistance().getText()
                );

                ridePlan.setEstimatedDuration(
                        matrix.getDuration().getText()
                );

                ridePlan.setEstimatedDurationInSeconds(
                        matrix.getDuration().getValue()
                );
            }
        }

        RidePlan saved = ridePlanRepository.save(ridePlan);

        triggerDriverNotifications(saved);

        return ApiResponse.success("RidePlan created successfully", mapToDto(saved));
    }

    private void triggerDriverNotifications(RidePlan ride) {

        if (ride.getRideStatus() != RideStatus.REQUESTED) return;

        if (ride.getRole() == null ||
                !"PARTNER".equalsIgnoreCase(ride.getRole().getName())) {
            return;
        }

        List<AvailableTaxi> activeDrivers =
                availableTaxiRepository.findByDriverStatus(DriverStatus.ONLINE);

        if (activeDrivers.isEmpty()) return;

        var notificationData = com.travelpartner.notification.dto.NewRideNotificationDto.builder()
                .rideId(ride.getId())
                .pickup(ride.getPickUpLocation())
                .drop(ride.getDropOffLocation())
                .city(ride.getCity())
                .fare(ride.getFare())
                .build();

        var event = com.travelpartner.notification.dto.SocketEvent.builder()
                .type("NEW_RIDE")
                .data(notificationData)
                .build();

        // FIX: prevent duplicate sends
        java.util.Set<Long> sentDrivers = new java.util.HashSet<>();

        for (AvailableTaxi taxi : activeDrivers) {

            Long driverId = taxi.getDriver().getId();

            if (!sentDrivers.add(driverId)) {
                continue;
            }

            System.out.println("Sending NEW_RIDE to driverId = " + driverId);

            rideRealtimeService.sendToUser(
                    driverId,
                    event
            );

            // NEW FIREBASE PUSH LOGIC

            User driver = taxi.getDriver();

            if (driver.getDeviceToken() != null &&
                    !driver.getDeviceToken().isBlank()) {

                firebasePushNotificationService.sendPushNotification(

                        com.travelpartner.notification.dto
                                .PushNotificationRequest
                                .builder()

                                .title("Traveling Partner")

                                .body("For you Plan is created by partner")

                                .time(
                                        java.time.LocalDateTime
                                                .now()
                                                .toString()
                                )

                                .token(driver.getDeviceToken())

                                .build()
                );
            }
        }
    }

    @Override
    public ApiResponse<Page<RidePlanDto>> getAllRidePlans(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

//        Page<RidePlanDto> result = ridePlanRepository
//                .findAllWithUserDriverAndBasicInfo(pageable)
//                .map(this::mapToDto);

        Page<RidePlanDto> result = ridePlanRepository
                .findAllBy(pageable)
                .map(this::mapToDto);

        return ApiResponse.success("RidePlans fetched successfully", result);
    }

    @Override
    public ApiResponse<RidePlanDto> getRidePlanById(Long id) {

        return ridePlanRepository.findByIdWithUserDriverAndBasicInfo(id)
                .map(plan -> ApiResponse.success(
                        "RidePlan fetched successfully",
                        mapToDto(plan)
                ))
                .orElse(ApiResponse.error(404, "RidePlan not found"));
    }

    @Override
    public ApiResponse<RidePlanDto> updateRidePlan(Long id, RidePlanDto dto) {
        RidePlan plan = ridePlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("RidePlan not found"));

        // update all fields
        plan = mapToEntity(dto);
        plan.setId(id);

        RidePlan updated = ridePlanRepository.save(plan);
        return ApiResponse.success("RidePlan updated successfully", mapToDto(updated));
    }

    @Override
    public ApiResponse<Void> deleteRidePlan(Long id) {
        RidePlan plan = ridePlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("RidePlan not found"));
        ridePlanRepository.delete(plan);
        return ApiResponse.success("RidePlan deleted successfully", null);
    }

    @Override
    public ApiResponse<RidePlanDto> updateRideStatus(Long id, RideStatus status, Long driverId) {

        RidePlan plan = ridePlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("RidePlan not found"));

        // 1. BLOCK duplicate accept
        if (plan.getRideStatus() == RideStatus.ACCEPTED) {
            return ApiResponse.error(409, "Ride already accepted by another driver");
        }

        // 2. DRIVER ACCEPT FLOW
        if (status == RideStatus.ACCEPTED) {

            if (driverId == null) {
                return ApiResponse.error(400, "DriverId is required to accept ride");
            }

            User driver = userRepository.findById(driverId)
                    .orElseThrow(() -> new IllegalArgumentException("Driver not found"));

            plan.setDriver(driver);
            plan.setRideStatus(RideStatus.ACCEPTED);
        }

        // 3. OTHER STATUS FLOW
        else {
            plan.setRideStatus(status);
        }

        RidePlan updated = ridePlanRepository.save(plan);

        // 4. Notify partner ONLY when accepted
        if (status == RideStatus.ACCEPTED) {
            notifyPartner(plan);
        }

        return ApiResponse.success("Ride status updated successfully", mapToDto(updated));
    }

    private void notifyPartner(RidePlan ride) {

        if (ride.getUser() == null) return;

        Long partnerId = ride.getUser().getId();

        var payload = com.travelpartner.notification.dto.SocketEvent.builder()
                .type("RIDE_ACCEPTED")
                .data(java.util.Map.of(
                        "rideId", ride.getId(),
                        "driverId", ride.getDriver().getId(),
                        "driverName", ride.getDriver().getBasicInformation() != null
                                ? ride.getDriver().getBasicInformation().getFirstName()
                                : null,
                        "status", "ACCEPTED"
                ))
                .build();

        rideRealtimeService.sendToUser(
                partnerId,
                payload
        );
    }

    @Override
    public ApiResponse<Page<RidePlanDto>> getRidePlansByDriverAndStatus(
            Long driverId,
            RideStatus status,
            int page,
            int size
    ) {

        if (!userRepository.existsById(driverId)) {
            return ApiResponse.error(404, "Driver not found");
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<RidePlanDto> result = ridePlanRepository
                .findByDriver_IdAndRideStatus(
                        driverId,
                        status,
                        pageable
                )
                .map(this::mapToDto);

        return ApiResponse.success(
                "Driver ride plans fetched successfully",
                result
        );
    }

    @Override
    public ApiResponse<RidePlanDto> getUpcomingRide(Long driverId) {

        if (!userRepository.existsById(driverId)) {
            return ApiResponse.error(404, "Driver not found");
        }

        Pageable pageable = PageRequest.of(0, 1);

        List<RidePlan> rides =
                ridePlanRepository.findUpcomingAcceptedRide(driverId, pageable);

        if (rides.isEmpty()) {
            return ApiResponse.error(404, "No upcoming accepted ride found");
        }

        return ApiResponse.success(
                "Upcoming ride fetched successfully",
                mapToDto(rides.get(0))
        );
    }

    @Override
    public ApiResponse<Page<RidePlanDto>> getRidePlansByUserTypeAndStatus(
            Long userId,
            String type,
            RideStatus status,
            int page,
            int size
    ) {

        if (!userRepository.existsById(userId)) {
            return ApiResponse.error(404, "User not found");
        }

        if (type == null ||
                (!type.equalsIgnoreCase("PARTNER")
                        && !type.equalsIgnoreCase("DRIVER"))) {

            return ApiResponse.error(
                    400,
                    "Type must be PARTNER or DRIVER"
            );
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<RidePlanDto> result = ridePlanRepository
                .findRidePlansByUserTypeAndStatus(
                        userId,
                        type.toUpperCase(),
                        status,
                        pageable
                )
                .map(this::mapToDto);

        return ApiResponse.success(
                "Ride plans fetched successfully",
                result
        );
    }

    private VehicleDto mapVehicleToDto(Vehicle vehicle) {

        if (vehicle == null) {
            return null;
        }

        return VehicleDto.builder()
                .id(vehicle.getId())

                .modelNumberId(
                        vehicle.getModelNumber() != null
                                ? vehicle.getModelNumber().getId()
                                : null
                )

                .modelNumberName(
                        vehicle.getModelNumber() != null
                                ? vehicle.getModelNumber().getName()
                                : null
                )

                .colorId(
                        vehicle.getColor() != null
                                ? vehicle.getColor().getId()
                                : null
                )

                .colorName(
                        vehicle.getColor() != null
                                ? vehicle.getColor().getName()
                                : null
                )

                .registrationNo(vehicle.getRegistrationNo())
                .registrationFront(vehicle.getRegistrationFront())
                .registrationBack(vehicle.getRegistrationBack())

                .outdoorImages(vehicle.getOutdoorImages())
                .indoorImages(vehicle.getIndoorImages())

                .ac(vehicle.isAc())
                .petsAllowed(vehicle.isPetsAllowed())
                .smokingAllowed(vehicle.isSmokingAllowed())

                .vehicleVerified(vehicle.getVehicleVerified())

                .brandId(
                        vehicle.getBrand() != null
                                ? vehicle.getBrand().getId()
                                : null
                )

                .brandName(
                        vehicle.getBrand() != null
                                ? vehicle.getBrand().getName()
                                : null
                )

                .userId(
                        vehicle.getUser() != null
                                ? vehicle.getUser().getId()
                                : null
                )

                .build();
    }
}