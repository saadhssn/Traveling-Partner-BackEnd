package com.travelpartner.auth.service;

public interface RoleAssignService {

    void assignRoleToUser(String roleName, Long userId);
}
