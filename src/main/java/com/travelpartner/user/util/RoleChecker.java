package com.travelpartner.user.util;

import com.travelpartner.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class RoleChecker {

    public boolean isAdmin(User user) {
        return user.getRoles().stream()
                .anyMatch(r -> r.getName().equalsIgnoreCase("ADMIN"));
    }
}
