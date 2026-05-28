package com.travelpartner.auth.service.impl;

import com.travelpartner.auth.dto.AdminSignupRequest;
import com.travelpartner.auth.dto.AppSignupRequest;
import com.travelpartner.auth.service.AuthService;
import com.travelpartner.role.model.Role;
import com.travelpartner.role.repository.RoleRepository;
import com.travelpartner.user.dto.UserResponse;
import com.travelpartner.user.enums.Platform;
import com.travelpartner.user.enums.UserStatus;
import com.travelpartner.user.model.User;
import com.travelpartner.user.repository.UserRepository;
import com.travelpartner.config.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /* ================= ADMIN SIGNUP ================= */
    @Override
    public UserResponse adminSignup(AdminSignupRequest request) {

        // ===== REQUIRED =====
        if (request.getMobileNumber() == null || request.getMobileNumber().isBlank()) {
            throw new RuntimeException("Mobile number is required");
        }

        // ===== GLOBAL DUPLICATE CHECKS =====
        if (request.getMobileNumber() != null &&
                userRepository.existsByMobileNumber(request.getMobileNumber())) {

            User existing = userRepository.findByMobileNumber(request.getMobileNumber()).get();

            // If existing user is ADMIN → allow update flow
            boolean isAdmin = existing.getRoles().stream()
                    .anyMatch(r -> "ADMIN".equalsIgnoreCase(r.getSlug()));

            if (!isAdmin) {
                throw new RuntimeException("Mobile number already registered with another account");
            }
        }

        if (request.getEmail() != null &&
                userRepository.existsByEmail(request.getEmail())) {

            User existing = userRepository.findByEmail(request.getEmail()).get();

            if (!existing.getMobileNumber().equals(request.getMobileNumber())) {
                throw new RuntimeException("Email already assigned to another user");
            }
        }

        if (request.getUsername() != null &&
                userRepository.existsByUsername(request.getUsername())) {

            User existing = userRepository.findByUsername(request.getUsername()).get();

            if (!existing.getMobileNumber().equals(request.getMobileNumber())) {
                throw new RuntimeException("Username already taken");
            }
        }

        // ===== FIND EXISTING USER =====
        User user = userRepository.findByMobileNumber(request.getMobileNumber())
                .orElse(null);

        Role adminRole = roleRepository.findBySlug("ADMIN")
                .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

        if (user == null) {

            // ===== CREATE USER =====
            user = User.builder()
                    .email(request.getEmail())
                    .username(request.getUsername())
                    .mobileNumber(request.getMobileNumber())
                    .password(request.getPassword() != null
                            ? passwordEncoder.encode(request.getPassword())
                            : null)
                    .platform(Platform.ADMIN_PORTAL)
                    .status(UserStatus.ACTIVE)
                    .isOtpVerified(true)
                    .roles(Set.of(adminRole))
                    .build();

        } else {

            // ===== UPDATE EXISTING USER =====
            if (request.getEmail() != null)
                user.setEmail(request.getEmail());

            if (request.getUsername() != null)
                user.setUsername(request.getUsername());

            if (request.getPassword() != null)
                user.setPassword(passwordEncoder.encode(request.getPassword()));

            // ensure ADMIN role exists
            user.getRoles().add(adminRole);
        }

        // ===== STATIC OTP =====
        String otp = "1234";
        user.setOtp(otp);

        User saved = userRepository.save(user);

        String token = jwtService.generateToken(
                saved.getId(),
                "ADMIN",
                saved.getEmail(),          // subject
                saved.getMobileNumber()    // mobileNumber claim
        );

        return UserResponse.builder()
                .id(saved.getId())
                .email(saved.getEmail())
                .username(saved.getUsername())
                .mobileNumber(saved.getMobileNumber())
                .status(saved.getStatus())
                .platform(saved.getPlatform())
                .roles(saved.getRoles().stream().map(Role::getSlug).collect(Collectors.toSet()))
                .otp(otp)
                .token(token)
                .build();
    }

//    @Override
//    public UserResponse adminSignup(AdminSignupRequest request) {
//
//        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
//            throw new RuntimeException("Email already exists");
//        }
//
//        if (request.getUsername() != null &&
//                userRepository.existsByUsername(request.getUsername())) {
//            throw new RuntimeException("Username already exists");
//        }
//
//        Role adminRole = roleRepository.findBySlug("ADMIN")
//                .orElseThrow(() -> new RuntimeException("ADMIN role not found"));
//
//        User user = User.builder()
//                .email(request.getEmail())
//                .username(request.getUsername())
//                .mobileNumber(request.getMobileNumber())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .platform(Platform.ADMIN_PORTAL)
//                .status(UserStatus.ACTIVE)
//                .isOtpVerified(true)
//                .roles(Set.of(adminRole))
//                .build();
//
//        User saved = userRepository.save(user);
//
//        // Generate JWT immediately
//        String token = jwtService.generateToken(saved.getId(), "ADMIN", saved.getEmail());
//
//        return mapToResponse(saved, token);
//    }

    /* ================= APP SIGNUP ================= */
    @Override
    public UserResponse appSignup(AppSignupRequest request) {

        if (request.getMobileNumber() == null || request.getMobileNumber().isBlank()) {
            throw new RuntimeException("Mobile number is required");
        }

        // ===== DUPLICATE CHECKS =====

        if (request.getEmail() != null &&
                userRepository.existsByEmail(request.getEmail())) {

            User existing = userRepository.findByEmail(request.getEmail()).get();

            if (!existing.getMobileNumber().equals(request.getMobileNumber())) {
                throw new RuntimeException("Email already assigned to another user");
            }
        }

        if (request.getUsername() != null &&
                userRepository.existsByUsername(request.getUsername())) {

            User existing = userRepository.findByUsername(request.getUsername()).get();

            if (!existing.getMobileNumber().equals(request.getMobileNumber())) {
                throw new RuntimeException("Username already taken");
            }
        }

        User user = userRepository.findByMobileNumber(request.getMobileNumber())
                .orElse(null);

        /* ================= REFERRAL LOGIC ================= */

        User referredAgent = null;

        if (request.getReferralCode() != null && !request.getReferralCode().isBlank()) {
            referredAgent = userRepository.findByReferralCode(request.getReferralCode())
                    .orElseThrow(() -> new RuntimeException("Invalid referral code"));
        }

        /* ================= CREATE ================= */

        if (user == null) {

            user = User.builder()
                    .email(request.getEmail())
                    .username(request.getUsername())
                    .mobileNumber(request.getMobileNumber())
                    .password(request.getPassword() != null
                            ? passwordEncoder.encode(request.getPassword())
                            : null)
                    .platform(Platform.MOBILE_APP)
                    .status(UserStatus.PENDING)
                    .isOtpVerified(false)
                    .referredByAgent(referredAgent)
                    .build();

            userRepository.save(user);
        }

        /* ================= OTP ================= */

        String otp = generateOrUpdateOtp(user);

        /* ================= TOKEN ================= */

        String token = null;

        if (user.getPassword() != null && Boolean.TRUE.equals(user.getIsOtpVerified())) {

            String roleName = user.getRoles().isEmpty()
                    ? "USER"
                    : user.getRoles().stream().findFirst().get().getSlug();

            token = jwtService.generateToken(user.getId(), roleName, user.getEmail(), user.getMobileNumber());
        }

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .mobileNumber(user.getMobileNumber())
                .status(user.getStatus())
                .platform(user.getPlatform())
                .roles(user.getRoles().stream().map(Role::getSlug).collect(Collectors.toSet()))
                .otp(otp)
                .token(token)
                .build();
    }
    /* ================= GENERATE OR UPDATE OTP ================= */
//    private String generateOrUpdateOtp(User user) {
//        String otp = String.valueOf((int) (Math.random() * 9000) + 1000); // 4-digit OTP
//        user.setOtp(otp);
//        userRepository.save(user);
//        return otp;
//    }

    private String generateOrUpdateOtp(User user) {

        // STATIC OTP FOR DEVELOPMENT
        String otp = "1234";

        user.setOtp(otp);
        userRepository.save(user);

        // SMS DISABLED FOR NOW
        // smsService.sendOtp(user.getMobileNumber(), otp);

        return otp;
    }

    /* ================= COMMON RESPONSE MAPPER ================= */
    private UserResponse mapToResponse(User user, String token) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .mobileNumber(user.getMobileNumber())
                .status(user.getStatus())
                .platform(user.getPlatform())
                .roles(user.getRoles().isEmpty() ? new HashSet<>() :
                        user.getRoles().stream().map(Role::getSlug).collect(Collectors.toSet()))
                .token(token)
                .build();
    }
}
