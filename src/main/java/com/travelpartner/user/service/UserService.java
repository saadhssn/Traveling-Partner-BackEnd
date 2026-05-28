package com.travelpartner.user.service;

import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.user.dto.*;

import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    UserResponse getUserById(Long id);

    UserResponse getUserByMobile(String mobileNumber);

    List<UserResponse> getAllActiveUsers();

    List<UserResponse> getAllUsersIncludingDeleted();

    void softDeleteUser(Long id);

    void hardDeleteUser(Long id);

    UserResponse updateUser(Long id, UpdateUserRequest request);

    void restoreUser(Long id); // restore soft deleted

    Page<UserResponse> getDrivers(UserFilterRequest filter, int page, int size);

    DriverFullResponse getDriverFullById(Long id);

    void updateUserStatus(Long userId, UpdateUserStatusRequest request);

    Page<UserDocumentResponse> getAllDocuments(int page, int size);

    UserDocumentResponse getUserDocumentsById(Long userId);

    void updateDocumentStatus(Long userId, UpdateDocumentStatusRequest request);

    Page<SaleAgentResponse> getAllSaleAgents(SaleAgentFilterRequest filter, int page, int size);

    SaleAgentResponse getSaleAgentById(Long id);

    Page<PartnerResponse> getPartners(UserFilterRequest filter, int page, int size);

    PartnerFullResponse getPartnerFullById(Long id);

    DashboardCountResponse getDashboardCounts();

    DriverStatusCountResponse getDriverStatusCounts();

    RidePlanStatusCountResponse getRidePlanStatusCounts();

    RidePlanGraphResponse getRidePlanLast14DaysGraph();

    Page<DriverDocumentTypeResponse> getDriverDocumentTypes(UserFilterRequest filter, int page, int size);

    ApiResponse<String> updateDeviceToken(
            Long userId,
            String deviceToken
    );
}
