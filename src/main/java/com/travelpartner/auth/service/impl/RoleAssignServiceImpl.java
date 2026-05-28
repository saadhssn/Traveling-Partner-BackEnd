package com.travelpartner.auth.service.impl;

import com.travelpartner.auth.service.RoleAssignService;
import com.travelpartner.role.model.Role;
import com.travelpartner.role.repository.RoleRepository;
import com.travelpartner.user.model.User;
import com.travelpartner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleAssignServiceImpl implements RoleAssignService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public void assignRoleToUser(String roleName, Long userId) {
        // Check if the role exists
        Optional<Role> roleOpt = roleRepository.findByName(roleName);
        if (roleOpt.isEmpty()) {
            throw new RuntimeException("Role '" + roleName + "' does not exist.");
        }

        Role role = roleOpt.get();

        // Find the user
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User with ID " + userId + " not found.");
        }

        User user = userOpt.get();

        // Assign role
        user.getRoles().add(role);  // add to existing roles
        userRepository.save(user);
    }
}
