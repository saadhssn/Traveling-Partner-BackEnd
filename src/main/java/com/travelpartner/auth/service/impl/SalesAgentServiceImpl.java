package com.travelpartner.auth.service.impl;

import com.travelpartner.auth.dto.CreateSalesAgentRequest;
import com.travelpartner.auth.dto.UpdateSalesAgentRequest;
import com.travelpartner.auth.service.SalesAgentService;
import com.travelpartner.common.util.ReferralCodeGenerator;
import com.travelpartner.role.model.Role;
import com.travelpartner.role.repository.RoleRepository;
import com.travelpartner.user.dto.UserResponse;
import com.travelpartner.user.enums.Platform;
import com.travelpartner.user.enums.UserStatus;
import com.travelpartner.user.model.User;
import com.travelpartner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesAgentServiceImpl implements SalesAgentService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createSalesAgent(CreateSalesAgentRequest request) {

        // ===== UNIQUE CHECKS =====

        if (request.getEmail() != null &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (request.getUsername() != null &&
                userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (request.getMobileNumber() != null &&
                userRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new RuntimeException("Mobile number already exists");
        }

        if (request.getCnicNumber() != null &&
                userRepository.existsByCnicNumber(request.getCnicNumber())) {
            throw new RuntimeException("CNIC already exists");
        }

        // ===== ROLE =====

        Role role = roleRepository.findBySlug("SALES_AGENT") // FIXED slug
                .orElseThrow(() -> new RuntimeException("SALES_AGENT role not found"));

        String referralCode = ReferralCodeGenerator.generateCode(request.getName());

        // ===== STATUS =====

        UserStatus status = request.getStatus() != null
                ? UserStatus.valueOf(request.getStatus().toUpperCase())
                : UserStatus.ACTIVE;

        // ===== CREATE USER =====

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .mobileNumber(request.getMobileNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .gender(request.getGender())
                .cnicNumber(request.getCnicNumber())
                .cnicFront(request.getCnicFront())
                .cnicBack(request.getCnicBack())
                .platform(Platform.ADMIN_PORTAL)
                .status(status)
                .isOtpVerified(true)
                .referralCode(referralCode)
                .roles(Set.of(role))
                .build();

        User saved = userRepository.save(user);

        return mapToResponse(saved);
    }

    @Override
    public UserResponse updateSalesAgent(Long id, UpdateSalesAgentRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sales agent not found"));

        // Ensure user is SALES_AGENT
        boolean isSalesAgent = user.getRoles().stream()
                .anyMatch(role -> role.getSlug().equals("SALES_AGENT"));

        if (!isSalesAgent) {
            throw new RuntimeException("User is not a sales agent");
        }

        // ===== UNIQUE CHECKS =====

        if (request.getEmail() != null && !request.getEmail().isBlank() &&
                !request.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmailAndIdNot(request.getEmail(), user.getId())) {
            throw new RuntimeException("Email already exists");
        }

        if (request.getUsername() != null && !request.getUsername().isBlank() &&
                !request.getUsername().equals(user.getUsername()) &&
                userRepository.existsByUsernameAndIdNot(request.getUsername(), user.getId())) {
            throw new RuntimeException("Username already exists");
        }

        if (request.getMobileNumber() != null && !request.getMobileNumber().isBlank() &&
                !request.getMobileNumber().equals(user.getMobileNumber()) &&
                userRepository.existsByMobileNumberAndIdNot(request.getMobileNumber(), user.getId())) {
            throw new RuntimeException("Mobile number already exists");
        }

        if (request.getCnicNumber() != null && !request.getCnicNumber().isBlank() &&
                !request.getCnicNumber().equals(user.getCnicNumber()) &&
                userRepository.existsByCnicNumberAndIdNot(request.getCnicNumber(), user.getId())) {
            throw new RuntimeException("CNIC already exists");
        }

        // ===== PARTIAL UPDATE =====

        if (request.getEmail() != null)
            user.setEmail(request.getEmail());

        if (request.getUsername() != null)
            user.setUsername(request.getUsername());

        if (request.getMobileNumber() != null)
            user.setMobileNumber(request.getMobileNumber());

        if (request.getName() != null)
            user.setName(request.getName());

        if (request.getGender() != null)
            user.setGender(request.getGender());

        if (request.getCnicNumber() != null)
            user.setCnicNumber(request.getCnicNumber());

        if (request.getCnicFront() != null)
            user.setCnicFront(request.getCnicFront());

        if (request.getCnicBack() != null)
            user.setCnicBack(request.getCnicBack());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getStatus() != null) {
            user.setStatus(UserStatus.valueOf(request.getStatus().toUpperCase()));
        }

        User updatedUser = userRepository.save(user);

        return mapToResponse(updatedUser);
    }

    @Override
    public List<UserResponse> getAllSalesAgents() {

        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getSlug().equals("SALES_AGENT")))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private UserResponse mapToResponse(User user) {

        Set<String> roles = user.getRoles()
                .stream()
                .map(Role::getSlug)
                .collect(Collectors.toSet());

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .name(user.getName())
                .mobileNumber(user.getMobileNumber())
                .status(user.getStatus())
                .platform(user.getPlatform())
                .roles(roles)
                .otp(user.getOtp()) // optional if needed
                .token(null) // optional
                .referralCode(user.getReferralCode()) // <--- add this
                .build();
    }
}