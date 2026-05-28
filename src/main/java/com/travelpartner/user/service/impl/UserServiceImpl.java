package com.travelpartner.user.service.impl;

import com.travelpartner.basicinformation.dto.BasicInformationDto;
import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.license.dto.LicenseDto;
import com.travelpartner.rideplan.enums.RideStatus;
import com.travelpartner.rideplan.repository.RidePlanRepository;
import com.travelpartner.user.dto.*;
import com.travelpartner.user.enums.DocumentStatus;
import com.travelpartner.user.enums.UserStatus;
import com.travelpartner.user.model.User;
import com.travelpartner.user.repository.UserRepository;
import com.travelpartner.user.service.UserService;
import com.travelpartner.user.specification.UserSpecification;
import com.travelpartner.vehicle.dto.VehicleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RidePlanRepository ridePlanRepository;

    /* ================= GET BY ID ================= */

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .filter(u -> !Boolean.TRUE.equals(u.getIsDeleted()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToResponse(user);
    }

    /* ================= GET BY MOBILE ================= */

    @Override
    public UserResponse getUserByMobile(String mobileNumber) {
        User user = userRepository.findByMobileNumber(mobileNumber)
                .filter(u -> !Boolean.TRUE.equals(u.getIsDeleted()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToResponse(user);
    }

    /* ================= GET ALL ACTIVE ================= */

    @Override
    public List<UserResponse> getAllActiveUsers() {
        return userRepository.findByIsDeletedFalse()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /* ================= GET ALL INCLUDING DELETED ================= */

    @Override
    public List<UserResponse> getAllUsersIncludingDeleted() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /* ================= SOFT DELETE ================= */

    @Override
    public void softDeleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsDeleted(true);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    /* ================= RESTORE USER ================= */

    @Override
    public void restoreUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsDeleted(false);
        user.setDeletedAt(null);
        userRepository.save(user);
    }

    /* ================= HARD DELETE ================= */

    @Override
    public void hardDeleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    /* ================= UPDATE USER ================= */

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new RuntimeException("Cannot update deleted user");
        }

        // ===== DUPLICATE CHECKS (ONLY IF VALUE CHANGED) =====

        if (request.getEmail() != null &&
                !request.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new RuntimeException("Email already in use by another user");
        }

        if (request.getUsername() != null &&
                !request.getUsername().equals(user.getUsername()) &&
                userRepository.existsByUsernameAndIdNot(request.getUsername(), id)) {
            throw new RuntimeException("Username already taken");
        }

        if (request.getMobileNumber() != null &&
                !request.getMobileNumber().equals(user.getMobileNumber()) &&
                userRepository.existsByMobileNumberAndIdNot(request.getMobileNumber(), id)) {
            throw new RuntimeException("Mobile number already registered");
        }

        if (request.getCnicNumber() != null &&
                !request.getCnicNumber().equals(user.getCnicNumber()) &&
                userRepository.existsByCnicNumberAndIdNot(request.getCnicNumber(), id)) {
            throw new RuntimeException("CNIC already registered");
        }

        // ===== UPDATE FIELDS =====

        if (request.getEmail() != null)
            user.setEmail(request.getEmail());

        if (request.getUsername() != null)
            user.setUsername(request.getUsername());

        if (request.getMobileNumber() != null)
            user.setMobileNumber(request.getMobileNumber());

        if (request.getCnicNumber() != null)
            user.setCnicNumber(request.getCnicNumber());

        if (request.getName() != null)
            user.setName(request.getName());

        if (request.getGender() != null)
            user.setGender(request.getGender());

        if (request.getDeviceToken() != null)
            user.setDeviceToken(request.getDeviceToken());

        userRepository.save(user);

        return mapToResponse(user);
    }

    @Override
    public Page<UserResponse> getDrivers(UserFilterRequest filter, int page, int size) {

        // Force DRIVER role
        filter.setRole("DRIVER");

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<User> usersPage = userRepository.findAll(
                UserSpecification.filterUsers(filter),
                pageable
        );

        return usersPage.map(this::mapToResponse);
    }

    /* ================= MAPPER ================= */

    private UserResponse mapToResponse(User user) {

        var basic = user.getBasicInformation();

        String fullName = null;

        if (basic != null) {
            String firstName = basic.getFirstName() != null
                    ? basic.getFirstName().trim()
                    : "";

            String lastName = basic.getLastName() != null
                    ? basic.getLastName().trim()
                    : "";

            fullName = (firstName + " " + lastName).trim();

            if (fullName.isBlank()) {
                fullName = null;
            }
        }

        return UserResponse.builder()
                .id(user.getId())

                // ===== EMAIL =====
                .email(
                        basic != null && basic.getEmail() != null
                                ? basic.getEmail()
                                : user.getEmail()
                )

                // ===== NAME =====
                .name(
                        fullName != null
                                ? fullName
                                : user.getName()
                )

                .username(user.getUsername())
                .mobileNumber(user.getMobileNumber())
                .status(user.getStatus())
                .platform(user.getPlatform())

                .roles(
                        user.getRoles()
                                .stream()
                                .map(r -> r.getSlug())
                                .collect(Collectors.toSet())
                )

                .otp(user.getOtp())

                // ===== GENDER =====
                .gender(
                        basic != null && basic.getGender() != null
                                ? basic.getGender()
                                : user.getGender()
                )

                // ===== CNIC =====
                .cnicNumber(
                        basic != null && basic.getCnicNumber() != null
                                ? basic.getCnicNumber()
                                : user.getCnicNumber()
                )

                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())

                // ===== CITY =====
                .city(
                        basic != null
                                ? basic.getCity()
                                : null
                )

                .build();
    }

    @Override
    public DriverFullResponse getDriverFullById(Long id) {
        User user = userRepository.findById(id)
                .filter(u -> !Boolean.TRUE.equals(u.getIsDeleted()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToDriverFullResponse(user);
    }

    private DriverFullResponse mapToDriverFullResponse(User user) {
        return DriverFullResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .mobileNumber(user.getMobileNumber())
                .status(user.getStatus())
                .platform(user.getPlatform())
                .roles(user.getRoles().stream().map(r -> r.getSlug()).collect(Collectors.toSet()))
                .otp(user.getOtp())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .referralCode(user.getReferralCode())
                .basicInformation(user.getBasicInformation() != null ? mapBasicInformation(user.getBasicInformation()) : null)
                .license(user.getLicense() != null ? mapLicense(user.getLicense()) : null)
                .vehicle(user.getVehicle() != null ? mapVehicle(user.getVehicle()) : null)
                .build();
    }

    private BasicInformationDto mapBasicInformation(com.travelpartner.basicinformation.model.BasicInformation basic) {
        return BasicInformationDto.builder()
                .userId(basic.getUser() != null ? basic.getUser().getId() : null)
                .firstName(basic.getFirstName())
                .lastName(basic.getLastName())
                .gender(basic.getGender())
                .whatsApp(basic.getWhatsApp())
                .email(basic.getEmail())
                .cnicNumber(basic.getCnicNumber())
                .cnicFront(basic.getCnicFront())
                .cnicBack(basic.getCnicBack())
                .city(basic.getCity())
                .profilePicture(basic.getProfilePicture())
                .referralCode(basic.getReferralCode())
                .acceptTerm(basic.getAcceptTerm())
                .build();
    }

    private LicenseDto mapLicense(com.travelpartner.license.model.License license) {
        return LicenseDto.builder()
                .userId(license.getUser() != null ? license.getUser().getId() : null)
                .licenseNo(license.getLicenseNo())
                .licenseFront(license.getLicenseFront())
                .licenseBack(license.getLicenseBack())
                .licenseVerified(license.getLicenseVerified())
                .build();
    }

    private VehicleDto mapVehicle(com.travelpartner.vehicle.model.Vehicle vehicle) {

        return VehicleDto.builder()
                .id(vehicle.getId())

                .userId(
                        vehicle.getUser() != null
                                ? vehicle.getUser().getId()
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

                // ===== BRAND =====
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

                // ===== MODEL NUMBER =====
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

                // ===== COLOR =====
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

                .build();
    }

    @Override
    public void updateUserStatus(Long userId, UpdateUserStatusRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("Updating user " + userId + " to status " + request.getStatus()); // <- debug log

        user.setStatus(request.getStatus());
        userRepository.save(user);

        System.out.println("User status updated successfully");
    }

    @Override
    public Page<UserDocumentResponse> getAllDocuments(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<User> users = userRepository.findAll(pageable);

        return users.map(user -> {

            String role = user.getRoles().stream()
                    .findFirst()
                    .map(r -> r.getSlug())
                    .orElse("USER");

            UserDocumentResponse.UserDocumentResponseBuilder builder = UserDocumentResponse.builder()
                    .id(user.getId())
                    .mobileNumber(user.getMobileNumber())
                    .roles(user.getRoles().stream().map(r -> r.getSlug()).collect(Collectors.toSet()))
                    .name(user.getName())
                    .gender(user.getGender());

            // ===== PARTNER =====
            if ("PARTNER".equalsIgnoreCase(role)) {

                if (user.getBasicInformation() != null) {
                    builder.profilePicture(user.getBasicInformation().getProfilePicture());
                }
            }

            // ===== DRIVER =====
            if ("DRIVER".equalsIgnoreCase(role)) {

                if (user.getBasicInformation() != null) {
                    builder.profilePicture(user.getBasicInformation().getProfilePicture());
                }

                if (user.getLicense() != null) {
                    builder.licenseFront(user.getLicense().getLicenseFront());
                    builder.licenseBack(user.getLicense().getLicenseBack());
                }

                if (user.getVehicle() != null) {
                    builder.registrationFront(user.getVehicle().getRegistrationFront());
                    builder.registrationBack(user.getVehicle().getRegistrationBack());
                    builder.outdoorImages(user.getVehicle().getOutdoorImages());
                    builder.indoorImages(user.getVehicle().getIndoorImages());
                }
            }

            return builder.build();
        });
    }

    @Override
    public UserDocumentResponse getUserDocumentsById(Long userId) {

        List<DocumentItemDto> documents = new ArrayList<>();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String role = user.getRoles().stream()
                .findFirst()
                .map(r -> r.getSlug())
                .orElse("USER");

        UserDocumentResponse.UserDocumentResponseBuilder builder =
                UserDocumentResponse.builder()
                        .id(user.getId())
                        .mobileNumber(user.getMobileNumber())
                        .roles(
                                user.getRoles()
                                        .stream()
                                        .map(r -> r.getSlug())
                                        .collect(Collectors.toSet())
                        )
                        .name(user.getName())
                        .gender(user.getGender())
                        .cnicStatus(user.getCnicStatus())
                        .licenseStatus(user.getLicenseStatus())
                        .vehicleDocStatus(user.getVehicleDocStatus());

/* =========================================================
   COMMON BASIC INFORMATION (PARTNER + DRIVER)
   ========================================================= */

        if (user.getBasicInformation() != null) {

            // ===== CNIC FRONT =====
            if (user.getBasicInformation().getCnicFront() != null) {

                documents.add(
                        DocumentItemDto.builder()
                                .documentType("CNIC")
                                .side("FRONT")
                                .url(user.getBasicInformation().getCnicFront())
                                .status(user.getCnicStatus())
                                .build()
                );
            }

            // ===== CNIC BACK =====
            if (user.getBasicInformation().getCnicBack() != null) {

                documents.add(
                        DocumentItemDto.builder()
                                .documentType("CNIC")
                                .side("BACK")
                                .url(user.getBasicInformation().getCnicBack())
                                .status(user.getCnicStatus())
                                .build()
                );
            }

            builder.profilePicture(user.getBasicInformation().getProfilePicture());
            builder.cnicFront(user.getBasicInformation().getCnicFront());
            builder.cnicBack(user.getBasicInformation().getCnicBack());
        }

/* =========================================================
   DRIVER DOCUMENTS ONLY
   ========================================================= */

        if ("DRIVER".equalsIgnoreCase(role)) {

            // ===== LICENSE =====
            if (user.getLicense() != null) {

                if (user.getLicense().getLicenseFront() != null) {

                    documents.add(
                            DocumentItemDto.builder()
                                    .documentType("LICENSE")
                                    .side("FRONT")
                                    .url(user.getLicense().getLicenseFront())
                                    .status(user.getLicenseStatus())
                                    .build()
                    );
                }

                if (user.getLicense().getLicenseBack() != null) {

                    documents.add(
                            DocumentItemDto.builder()
                                    .documentType("LICENSE")
                                    .side("BACK")
                                    .url(user.getLicense().getLicenseBack())
                                    .status(user.getLicenseStatus())
                                    .build()
                    );
                }

                builder.licenseFront(user.getLicense().getLicenseFront());
                builder.licenseBack(user.getLicense().getLicenseBack());
            }

            // ===== VEHICLE =====
            if (user.getVehicle() != null) {

                if (user.getVehicle().getRegistrationFront() != null) {

                    documents.add(
                            DocumentItemDto.builder()
                                    .documentType("REGISTRATION")
                                    .side("FRONT")
                                    .url(user.getVehicle().getRegistrationFront())
                                    .status(user.getVehicleDocStatus())
                                    .build()
                    );
                }

                if (user.getVehicle().getRegistrationBack() != null) {

                    documents.add(
                            DocumentItemDto.builder()
                                    .documentType("REGISTRATION")
                                    .side("BACK")
                                    .url(user.getVehicle().getRegistrationBack())
                                    .status(user.getVehicleDocStatus())
                                    .build()
                    );
                }

                builder.registrationFront(user.getVehicle().getRegistrationFront());
                builder.registrationBack(user.getVehicle().getRegistrationBack());
                builder.outdoorImages(user.getVehicle().getOutdoorImages());
                builder.indoorImages(user.getVehicle().getIndoorImages());
            }
        }

        builder.documents(documents);

        return builder.build();
    }

    @Override
    public void updateDocumentStatus(Long userId, UpdateDocumentStatusRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ===== CNIC STATUS =====
        if (request.getCnicStatus() != null) {
            user.setCnicStatus(request.getCnicStatus());
        }

        // ===== LICENSE STATUS =====
        if (request.getLicenseStatus() != null) {
            user.setLicenseStatus(request.getLicenseStatus());
        }

        // ===== VEHICLE STATUS =====
        if (request.getVehicleStatus() != null) {
            user.setVehicleDocStatus(request.getVehicleStatus());
        }

        // ===== REJECTION REASON =====
        if (request.getRejectionReason() != null) {
            user.setDocumentRejectionReason(request.getRejectionReason());
        }

        // ===== AUTO APPROVAL LOGIC =====
        if (user.getCnicStatus() == DocumentStatus.APPROVED &&
                user.getLicenseStatus() == DocumentStatus.APPROVED &&
                user.getVehicleDocStatus() == DocumentStatus.APPROVED) {

            user.setStatus(UserStatus.APPROVED);
        }

        userRepository.save(user);
    }

    @Override
    public Page<SaleAgentResponse> getAllSaleAgents(SaleAgentFilterRequest filter, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<User> users = userRepository.findAll(
                UserSpecification.filterSaleAgents(filter),
                pageable
        );

        return users.map(user -> SaleAgentResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .mobileNumber(user.getMobileNumber())
                .name(user.getName())
                .status(user.getStatus())
                .cnicNumber(user.getCnicNumber())
                .cnicFront(user.getCnicFront())
                .cnicBack(user.getCnicBack())
                .roles(
                        user.getRoles()
                                .stream()
                                .map(role -> role.getSlug())
                                .collect(Collectors.toSet())
                )
                .build()
        );
    }

    @Override
    public SaleAgentResponse getSaleAgentById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Ensure only SALES_AGENT is returned
        boolean isSaleAgent = user.getRoles()
                .stream()
                .anyMatch(role -> "SALES_AGENT".equalsIgnoreCase(role.getSlug()));

        if (!isSaleAgent) {
            throw new RuntimeException("User is not a SALES_AGENT");
        }

        return SaleAgentResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .mobileNumber(user.getMobileNumber())
                .name(user.getName())
                .status(user.getStatus())
                .cnicNumber(user.getCnicNumber())
                .cnicFront(user.getCnicFront())
                .cnicBack(user.getCnicBack())

                // roles
                .roles(
                        user.getRoles()
                                .stream()
                                .map(role -> role.getSlug())
                                .collect(Collectors.toSet())
                )
                .build();
    }

    @Override
    public Page<PartnerResponse> getPartners(UserFilterRequest filter, int page, int size) {

        filter.setRole("PARTNER"); // force partner only

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<User> users = userRepository.findAll(
                UserSpecification.filterUsers(filter),
                pageable
        );

        return users.map(user -> {

            var basic = user.getBasicInformation();

            String name = (basic != null)
                    ? ( (basic.getFirstName() != null ? basic.getFirstName() : "") +
                    " " +
                    (basic.getLastName() != null ? basic.getLastName() : "") ).trim()
                    : user.getName();

            String email = (basic != null && basic.getEmail() != null)
                    ? basic.getEmail()
                    : user.getEmail();

            String cnicNumber = (basic != null)
                    ? basic.getCnicNumber()
                    : null;

            String city = (basic != null)
                    ? basic.getCity()
                    : null;

            String profilePicture = (basic != null)
                    ? basic.getProfilePicture()
                    : null;

            String cnicFront = (basic != null)
                    ? basic.getCnicFront()
                    : null;

            String cnicBack = (basic != null)
                    ? basic.getCnicBack()
                    : null;

            return PartnerResponse.builder()
                    .id(user.getId())
                    .name(name)
                    .email(email)
                    .mobileNumber(user.getMobileNumber())
                    .status(user.getStatus())
                    .roles(user.getRoles().stream()
                            .map(r -> r.getSlug())
                            .collect(Collectors.toSet()))
                    .city(city)
                    .profilePicture(profilePicture)
                    .cnicNumber(cnicNumber)
                    .cnicFront(cnicFront)
                    .cnicBack(cnicBack)
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .build();
        });
    }

    @Override
    public PartnerFullResponse getPartnerFullById(Long id) {

        User user = userRepository.findById(id)
                .filter(u -> !Boolean.TRUE.equals(u.getIsDeleted()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Ensure only PARTNER role is allowed
        boolean isPartner = user.getRoles()
                .stream()
                .anyMatch(role -> "PARTNER".equalsIgnoreCase(role.getSlug()));

        if (!isPartner) {
            throw new RuntimeException("User is not a PARTNER");
        }

        return PartnerFullResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .mobileNumber(user.getMobileNumber())
                .status(user.getStatus())
                .platform(user.getPlatform())
                .roles(user.getRoles().stream()
                        .map(r -> r.getSlug())
                        .collect(Collectors.toSet()))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .referralCode(user.getReferralCode())

                // ONLY BASIC INFORMATION (NO LICENSE / VEHICLE)
                .basicInformation(user.getBasicInformation() != null
                        ? mapBasicInformation(user.getBasicInformation())
                        : null)

                .build();
    }

    @Override
    public DashboardCountResponse getDashboardCounts() {

        long drivers = userRepository
                .countByRoles_SlugIgnoreCaseAndIsDeletedFalse("DRIVER");

        long partners = userRepository
                .countByRoles_SlugIgnoreCaseAndIsDeletedFalse("PARTNER");

        long salesAgents = userRepository
                .countByRoles_SlugIgnoreCaseAndIsDeletedFalse("SALES_AGENT");

        long ridePlans = ridePlanRepository.count();

        return DashboardCountResponse.builder()
                .totalDrivers(drivers)
                .totalPartners(partners)
                .totalSalesAgents(salesAgents)
                .totalRidePlans(ridePlans)
                .build();
    }

    @Override
    public DriverStatusCountResponse getDriverStatusCounts() {

        String role = "DRIVER";

        long active = userRepository.countByRoles_SlugIgnoreCaseAndStatusAndIsDeletedFalse(role, UserStatus.ACTIVE);
        long inactive = userRepository.countByRoles_SlugIgnoreCaseAndStatusAndIsDeletedFalse(role, UserStatus.INACTIVE);
        long blocked = userRepository.countByRoles_SlugIgnoreCaseAndStatusAndIsDeletedFalse(role, UserStatus.BLOCKED);
        long pending = userRepository.countByRoles_SlugIgnoreCaseAndStatusAndIsDeletedFalse(role, UserStatus.PENDING);
        long approved = userRepository.countByRoles_SlugIgnoreCaseAndStatusAndIsDeletedFalse(role, UserStatus.APPROVED);

        return DriverStatusCountResponse.builder()
                .active(active)
                .inactive(inactive)
                .blocked(blocked)
                .pending(pending)
                .approved(approved)
                .build();
    }

    @Override
    public RidePlanStatusCountResponse getRidePlanStatusCounts() {

        List<Object[]> results = ridePlanRepository.countRidePlansByStatus();

        long requested = 0, accepted = 0, started = 0, completed = 0, canceled = 0;

        for (Object[] row : results) {
            RideStatus status = (RideStatus) row[0];
            long count = (long) row[1];

            switch (status) {
                case REQUESTED -> requested = count;
                case ACCEPTED -> accepted = count;
                case STARTED -> started = count;
                case COMPLETED -> completed = count;
                case CANCELED -> canceled = count;
            }
        }

        return RidePlanStatusCountResponse.builder()
                .requested(requested)
                .accepted(accepted)
                .started(started)
                .completed(completed)
                .canceled(canceled)
                .build();
    }

    @Override
    public RidePlanGraphResponse getRidePlanLast14DaysGraph() {

        LocalDateTime fromDate = LocalDateTime.now().minusDays(13);

        List<Object[]> results = ridePlanRepository.countRidePlansLastDays(fromDate);

        Map<String, Long> dataMap = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // fill DB results
        for (Object[] row : results) {
            String date = row[0].toString();
            Long count = (Long) row[1];
            dataMap.put(date, count);
        }

        List<String> dates = new ArrayList<>();
        List<Long> counts = new ArrayList<>();

        // build last 14 days (even if 0)
        for (int i = 13; i >= 0; i--) {
            String date = LocalDate.now().minusDays(i).format(formatter);

            dates.add(date);
            counts.add(dataMap.getOrDefault(date, 0L));
        }

        return RidePlanGraphResponse.builder()
                .dates(dates)
                .counts(counts)
                .build();
    }

    @Override
    public Page<DriverDocumentTypeResponse> getDriverDocumentTypes(UserFilterRequest filter, int page, int size) {

        // force DRIVER only
        filter.setRole("DRIVER");

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<User> users = userRepository.findAll(
                UserSpecification.filterUsers(filter),
                pageable
        );

        // flatten users → multiple document rows
        List<DriverDocumentTypeResponse> flatList = new ArrayList<>();

        for (User user : users.getContent()) {

            String name = user.getName();

            // CNIC always (driver)
            if (user.getBasicInformation() != null &&
                    (user.getBasicInformation().getCnicFront() != null ||
                            user.getBasicInformation().getCnicBack() != null)) {

                flatList.add(DriverDocumentTypeResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .name(name)
                        .mobileNumber(user.getMobileNumber())
                        .documentType("CNIC")
                        .status(user.getCnicStatus())
                        .createdAt(user.getCreatedAt())
                        .build());
            }

            // LICENSE
            if (user.getLicense() != null &&
                    (user.getLicense().getLicenseFront() != null ||
                            user.getLicense().getLicenseBack() != null)) {

                flatList.add(DriverDocumentTypeResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .name(name)
                        .mobileNumber(user.getMobileNumber())
                        .documentType("LICENSE")
                        .status(user.getCnicStatus())
                        .createdAt(user.getCreatedAt())
                        .build());
            }

            // VEHICLE REGISTRATION
            if (user.getVehicle() != null &&
                    (user.getVehicle().getRegistrationFront() != null ||
                            user.getVehicle().getRegistrationBack() != null)) {

                flatList.add(DriverDocumentTypeResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .name(name)
                        .mobileNumber(user.getMobileNumber())
                        .documentType("VEHICLE REGISTRATION")
                        .status(user.getCnicStatus())
                        .createdAt(user.getCreatedAt())
                        .build());
            }
        }

        return new PageImpl<>(flatList, pageable, users.getTotalElements());
    }

    @Override
    public ApiResponse<String> updateDeviceToken(
            Long userId,
            String deviceToken
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        user.setDeviceToken(deviceToken);

        userRepository.save(user);

        return ApiResponse.success(
                "Device token updated successfully",
                deviceToken
        );
    }
}
