package com.travelpartner.auth.service.impl;

import com.travelpartner.auth.dto.AdminLoginRequest;
import com.travelpartner.auth.dto.AdminLoginResponse;
import com.travelpartner.user.model.User;
import com.travelpartner.user.repository.UserRepository;
import com.travelpartner.config.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminAuthServiceImpl implements com.travelpartner.auth.service.AdminAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AdminLoginResponse login(AdminLoginRequest request) {

        User user = null;

        // ===== CASE 1: EMAIL + PASSWORD =====
        if (request.getEmail() != null && request.getPassword() != null) {

            user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }
        }

        // ===== CASE 2: MOBILE + PASSWORD =====
        else if (request.getMobileNumber() != null && request.getPassword() != null) {

            user = userRepository.findByMobileNumber(request.getMobileNumber())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }
        }

        // ===== CASE 3: MOBILE + OTP =====
        else if (request.getMobileNumber() != null && request.getOtp() != null) {

            user = userRepository.findByMobileNumber(request.getMobileNumber())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));

            // STATIC OTP CHECK
            if (!"1234".equals(request.getOtp())) {
                throw new RuntimeException("Invalid OTP");
            }
        }

        else {
            throw new RuntimeException("Invalid login request");
        }

        // ===== ROLE CHECK =====
        if (user.getRoles().isEmpty()) {
            throw new RuntimeException("No role assigned to this admin");
        }

        String role = user.getRoles().iterator().next().getSlug();

        // ===== TOKEN =====
        String token = jwtService.generateToken(
                user.getId(),
                role,
                user.getEmail(),
                user.getMobileNumber()
        );
        return AdminLoginResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .mobileNumber(user.getMobileNumber())
                .role(role)
                .token(token)
                .build();
    }

//    @Override
//    public AdminLoginResponse login(AdminLoginRequest request) {
//        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
//
//        if (userOpt.isEmpty()) {
//            throw new RuntimeException("Admin not found");
//        }
//
//        User user = userOpt.get();
//
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new RuntimeException("Invalid credentials");
//        }
//
//        if (user.getRoles().isEmpty()) {
//            throw new RuntimeException("No role assigned to this admin");
//        }
//
//        String role = user.getRoles().iterator().next().getSlug();
//        String token = jwtService.generateToken(user.getId(), role, user.getEmail());
//
//        return AdminLoginResponse.builder()
//                .id(user.getId())
//                .email(user.getEmail())
//                .role(role)
//                .token(token)
//                .build();
//    }
}
