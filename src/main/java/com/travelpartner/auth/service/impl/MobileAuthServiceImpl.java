package com.travelpartner.auth.service.impl;

import com.travelpartner.auth.dto.*;
import com.travelpartner.auth.service.MobileAuthService;
import com.travelpartner.basicinformation.dto.BasicInformationDto;
import com.travelpartner.basicinformation.repository.BasicInformationRepository;
import com.travelpartner.license.dto.LicenseDto;
import com.travelpartner.license.repository.LicenseRepository;
import com.travelpartner.user.model.User;
import com.travelpartner.user.repository.UserRepository;
import com.travelpartner.user.service.OtpService;
import com.travelpartner.user.enums.UserStatus;
import com.travelpartner.role.model.Role;
import com.travelpartner.config.security.JwtService;
import com.travelpartner.vehicle.dto.VehicleDto;
import com.travelpartner.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MobileAuthServiceImpl implements MobileAuthService {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final BasicInformationRepository basicInformationRepository;
    private final LicenseRepository licenseRepository;
    private final VehicleRepository vehicleRepository;

//    @Override
//    public VerifyOtpResponse verifyOtp(VerifyOtpRequest request) {
//
//        if (!otpService.validateOtp(request.getMobileNumber(), request.getOtp())) {
//            throw new RuntimeException("Invalid OTP");
//        }
//
//        User user = userRepository.findByMobileNumber(request.getMobileNumber())
//                .stream().findFirst()
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        user.setIsOtpVerified(true);
//
//        if (user.getStatus() == UserStatus.PENDING) {
//            user.setStatus(UserStatus.ACTIVE);
//        }
//
//        userRepository.save(user);
//
//        String roleName = user.getRoles().isEmpty() ? null :
//                user.getRoles().stream().findFirst().get().getSlug();
//
//        String token = jwtService.generateToken(user.getId(), roleName, user.getMobileNumber());
//
//        return VerifyOtpResponse.builder()
//                .id(user.getId())
//                .email(user.getEmail())
//                .username(user.getUsername())
//                .mobileNumber(user.getMobileNumber())
//                .status(user.getStatus())
//                .platform(user.getPlatform())
//                .roles(user.getRoles().stream().map(Role::getSlug).collect(Collectors.toSet()))
//                .token(token)
//
//                // JOURNEY STATUS
//                .basicInformationId(
//                        user.getBasicInformation() != null ?
//                                user.getBasicInformation().getId() : null
//                )
//                .licenseId(
//                        user.getLicense() != null ?
//                                user.getLicense().getId() : null
//                )
//                .vehicleId(
//                        user.getVehicle() != null ?
//                                user.getVehicle().getId() : null
//                )
//
//                // DOCUMENT STATUS
//                .cnicStatus(user.getCnicStatus())
//                .licenseStatus(user.getLicenseStatus())
//                .vehicleDocStatus(user.getVehicleDocStatus())
//
//                .build();
//    }

    @Override
    public VerifyOtpResponse verifyOtp(VerifyOtpRequest request) {

        if (!otpService.validateOtp(request.getMobileNumber(), request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        User user = userRepository.findByMobileNumber(request.getMobileNumber())
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsOtpVerified(true);

        if (user.getStatus() == UserStatus.PENDING) {
            user.setStatus(UserStatus.ACTIVE);
        }

        userRepository.save(user);

        String roleName = user.getRoles().isEmpty() ? null :
                user.getRoles().stream().findFirst().get().getSlug();

        // ===== ROLE FLAGS =====
        boolean isDriver = user.getRoles().stream()
                .anyMatch(r -> "DRIVER".equalsIgnoreCase(r.getSlug()));

        boolean isPartner = user.getRoles().stream()
                .anyMatch(r -> "PARTNER".equalsIgnoreCase(r.getSlug()));

        // ===== FETCH DATA =====

        // Basic Information
        BasicInformationDto basicDto = basicInformationRepository.findByUser(user)
                .map(b -> BasicInformationDto.builder()
                        .userId(user.getId())
                        .firstName(b.getFirstName())
                        .lastName(b.getLastName())
                        .gender(b.getGender())
                        .whatsApp(b.getWhatsApp())
                        .email(b.getEmail())
                        .cnicNumber(b.getCnicNumber())
                        .cnicFront(b.getCnicFront())
                        .cnicBack(b.getCnicBack())
                        .profilePicture(b.getProfilePicture())
                        .referralCode(b.getReferralCode())
                        .acceptTerm(b.getAcceptTerm())
                        .build())
                .orElse(null);

        // License
        LicenseDto licenseDto = licenseRepository.findByUser(user)
                .map(l -> LicenseDto.builder()
                        .userId(user.getId())
                        .licenseNo(l.getLicenseNo())
                        .licenseFront(l.getLicenseFront())
                        .licenseBack(l.getLicenseBack())
                        .licenseVerified(l.getLicenseVerified())
                        .build())
                .orElse(null);

        // Vehicle
        VehicleDto vehicleDto = vehicleRepository.findByUser(user)
                .map(v -> VehicleDto.builder()
                        .id(v.getId())
                        .modelNumberId(v.getModelNumber().getId())
                        .registrationNo(v.getRegistrationNo())
                        .registrationFront(v.getRegistrationFront())
                        .registrationBack(v.getRegistrationBack())
                        .outdoorImages(v.getOutdoorImages())
                        .indoorImages(v.getIndoorImages())
                        .ac(v.isAc())
                        .petsAllowed(v.isPetsAllowed())
                        .smokingAllowed(v.isSmokingAllowed())
                        .vehicleVerified(v.getVehicleVerified())
                        .brandId(v.getBrand().getId())
                        .userId(user.getId())
                        .build())
                .orElse(null);

        String token = jwtService.generateToken(user.getId(), roleName, user.getEmail(), user.getMobileNumber());

        return VerifyOtpResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .mobileNumber(user.getMobileNumber())
                .status(user.getStatus())
                .platform(user.getPlatform())
                .roles(user.getRoles().stream().map(Role::getSlug).collect(Collectors.toSet()))
                .token(token)

                // JOURNEY STATUS
                .basicInformationId(user.getBasicInformation() != null ? user.getBasicInformation().getId() : null)
                .licenseId(user.getLicense() != null ? user.getLicense().getId() : null)
                .vehicleId(user.getVehicle() != null ? user.getVehicle().getId() : null)

                // DOCUMENT STATUS
                .cnicStatus(user.getCnicStatus())
                .licenseStatus(user.getLicenseStatus())
                .vehicleDocStatus(user.getVehicleDocStatus())

                // NEW DATA (ROLE BASED)
                .basicInformation((isDriver || isPartner) ? basicDto : null)
                .license(isDriver ? licenseDto : null)
                .vehicle(isDriver ? vehicleDto : null)

                .build();
    }

    @Override
    public String setPassword(SetPasswordRequest request) {

        User user = userRepository.findByMobileNumber(request.getMobileNumber())
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (Boolean.FALSE.equals(user.getIsOtpVerified())) {
            throw new RuntimeException("OTP not verified. Please verify OTP first.");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return "Password set successfully. You can now login.";
    }

//    @Override
//    public Map<String, Object> login(AppLoginRequest request) {
//
//        User user = userRepository.findByMobileNumber(request.getMobileNumber())
//                .stream().findFirst()
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (user.getPassword() == null) {
//            throw new RuntimeException("Password not set. Please set password first.");
//        }
//
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new RuntimeException("Invalid password");
//        }
//
//        String roleName = user.getRoles().isEmpty() ? null :
//                user.getRoles().stream().findFirst().get().getSlug();
//
//        String token = jwtService.generateToken(user.getId(), roleName, user.getMobileNumber());
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("id", user.getId());
//        response.put("email", user.getEmail());
//        response.put("mobileNumber", user.getMobileNumber());
//        response.put("roles", user.getRoles().stream().map(Role::getSlug).toList());
//        response.put("token", token);
//
//        return response;
//    }

    @Override
    public Map<String, Object> login(AppLoginRequest request) {

        User user = userRepository.findByMobileNumber(request.getMobileNumber())
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getPassword() == null) {
            throw new RuntimeException("Password not set. Please set password first.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String roleName = user.getRoles().isEmpty() ? null :
                user.getRoles().stream().findFirst().get().getSlug();

        String token = jwtService.generateToken(user.getId(), roleName, user.getEmail(), user.getMobileNumber());

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("email", user.getEmail());
        response.put("username", user.getUsername());
        response.put("mobileNumber", user.getMobileNumber());
        response.put("roles", user.getRoles().stream().map(Role::getSlug).toList());
        response.put("token", token);

        // ROLE BASED DATA LOADING

        // Basic Information
        if ("PARTNER".equalsIgnoreCase(roleName) || "DRIVER".equalsIgnoreCase(roleName)) {
            BasicInformationDto basicDto = basicInformationRepository.findByUser(user)
                    .map(b -> BasicInformationDto.builder()
                            .userId(user.getId())
                            .firstName(b.getFirstName())
                            .lastName(b.getLastName())
                            .gender(b.getGender())
                            .whatsApp(b.getWhatsApp())
                            .email(b.getEmail())
                            .cnicNumber(b.getCnicNumber())
                            .cnicFront(b.getCnicFront())
                            .cnicBack(b.getCnicBack())
                            .profilePicture(b.getProfilePicture())
                            .referralCode(b.getReferralCode())
                            .acceptTerm(b.getAcceptTerm())
                            .build())
                    .orElse(null);

            response.put("basicInformation", basicDto);
        }

        // License & Vehicle only for Driver
        if ("DRIVER".equalsIgnoreCase(roleName)) {
            LicenseDto licenseDto = licenseRepository.findByUser(user)
                    .map(l -> LicenseDto.builder()
                            .userId(user.getId())
                            .licenseNo(l.getLicenseNo())
                            .licenseFront(l.getLicenseFront())
                            .licenseBack(l.getLicenseBack())
                            .licenseVerified(l.getLicenseVerified())
                            .build())
                    .orElse(null);

            response.put("license", licenseDto);
        }

        if ("DRIVER".equalsIgnoreCase(roleName)) {

            VehicleDto vehicleDto = vehicleRepository.findByUser(user)
                    .map(v -> VehicleDto.builder()
                            .id(v.getId())
                            .modelNumberId(v.getModelNumber().getId())
                            .registrationNo(v.getRegistrationNo())
                            .registrationFront(v.getRegistrationFront())
                            .registrationBack(v.getRegistrationBack())
                            .outdoorImages(v.getOutdoorImages())
                            .indoorImages(v.getIndoorImages())
                            .ac(v.isAc())
                            .petsAllowed(v.isPetsAllowed())
                            .smokingAllowed(v.isSmokingAllowed())
                            .vehicleVerified(v.getVehicleVerified())
                            .brandId(v.getBrand().getId())
                            .userId(user.getId())
                            .build())
                    .orElse(null);

            response.put("vehicle", vehicleDto);
        }

        return response;
    }

//    @Override
//    public Map<String, Object> login(AppLoginRequest request) {
//
//        User user = userRepository.findByMobileNumber(request.getMobileNumber())
//                .stream().findFirst()
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (user.getPassword() == null) {
//            throw new RuntimeException("Password not set. Please set password first.");
//        }
//
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new RuntimeException("Invalid password");
//        }
//
//        // ===== DOCUMENT APPROVAL CHECKS =====
//        if (user.getCnicStatus() != DocumentStatus.APPROVED) {
//            throw new RuntimeException("CNIC not approved. Reason: " + user.getCnicRejectionReason());
//        }
//        if (user.getLicenseStatus() != DocumentStatus.APPROVED) {
//            throw new RuntimeException("License not approved. Reason: " + user.getLicenseRejectionReason());
//        }
//        if (user.getVehicleDocStatus() != DocumentStatus.APPROVED) {
//            throw new RuntimeException("Vehicle documents not approved. Reason: " + user.getVehicleDocRejectionReason());
//        }
//        // ====================================
//
//        String roleName = user.getRoles().isEmpty() ? null :
//                user.getRoles().stream().findFirst().get().getSlug();
//
//        String token = jwtService.generateToken(user.getId(), roleName, user.getMobileNumber());
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("id", user.getId());
//        response.put("email", user.getEmail());
//        response.put("mobileNumber", user.getMobileNumber());
//        response.put("roles", user.getRoles().stream().map(Role::getSlug).toList());
//        response.put("token", token);
//
//        // ROLE BASED DATA LOADING
//        // Basic Information
//        if ("PARTNER".equalsIgnoreCase(roleName) || "DRIVER".equalsIgnoreCase(roleName)) {
//            BasicInformationDto basicDto = basicInformationRepository.findByUser(user)
//                    .map(b -> BasicInformationDto.builder()
//                            .userId(user.getId())
//                            .firstName(b.getFirstName())
//                            .lastName(b.getLastName())
//                            .gender(b.getGender())
//                            .whatsApp(b.getWhatsApp())
//                            .email(b.getEmail())
//                            .cnicNumber(b.getCnicNumber())
//                            .cnicFront(b.getCnicFront())
//                            .cnicBack(b.getCnicBack())
//                            .profilePicture(b.getProfilePicture())
//                            .referralCode(b.getReferralCode())
//                            .acceptTerm(b.getAcceptTerm())
//                            .build())
//                    .orElse(null);
//
//            response.put("basicInformation", basicDto);
//        }
//
//        // License & Vehicle only for Driver
//        if ("DRIVER".equalsIgnoreCase(roleName)) {
//            LicenseDto licenseDto = licenseRepository.findByUser(user)
//                    .map(l -> LicenseDto.builder()
//                            .userId(user.getId())
//                            .licenseNo(l.getLicenseNo())
//                            .licenseFront(l.getLicenseFront())
//                            .licenseBack(l.getLicenseBack())
//                            .licenseVerified(l.getLicenseVerified())
//                            .build())
//                    .orElse(null);
//
//            response.put("license", licenseDto);
//        }
//
//        if ("DRIVER".equalsIgnoreCase(roleName)) {
//            VehicleDto vehicleDto = vehicleRepository.findByUser(user)
//                    .map(v -> VehicleDto.builder()
//                            .id(v.getId())
//                            .modelNumberId(v.getModelNumber().getId())
//                            .registrationNo(v.getRegistrationNo())
//                            .registrationFront(v.getRegistrationFront())
//                            .registrationBack(v.getRegistrationBack())
//                            .outdoorImages(v.getOutdoorImages())
//                            .indoorImages(v.getIndoorImages())
//                            .ac(v.isAc())
//                            .petsAllowed(v.isPetsAllowed())
//                            .smokingAllowed(v.isSmokingAllowed())
//                            .vehicleVerified(v.getVehicleVerified())
//                            .brandId(v.getBrand().getId())
//                            .userId(user.getId())
//                            .build())
//                    .orElse(null);
//
//            response.put("vehicle", vehicleDto);
//        }
//
//        return response;
//    }
}
