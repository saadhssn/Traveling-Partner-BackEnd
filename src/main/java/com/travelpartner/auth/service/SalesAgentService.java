package com.travelpartner.auth.service;

import com.travelpartner.auth.dto.CreateSalesAgentRequest;
import com.travelpartner.auth.dto.UpdateSalesAgentRequest;
import com.travelpartner.user.dto.UserResponse;

import java.util.List;

public interface SalesAgentService {

    UserResponse createSalesAgent(CreateSalesAgentRequest request);

    UserResponse updateSalesAgent(Long id, UpdateSalesAgentRequest request);

    List<UserResponse> getAllSalesAgents();
}