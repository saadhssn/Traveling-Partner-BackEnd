package com.travelpartner.file.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.file.dto.DocumentStatusUpdateDto;
import com.travelpartner.file.service.FileUploadService;
import com.travelpartner.user.enums.DocumentStatus;
import com.travelpartner.user.model.User;
import com.travelpartner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/documents")
@RequiredArgsConstructor
public class AdminDocumentController {

    private final UserRepository userRepository;

    @PutMapping("/update-status/{userId}")
    @AuditAction(action = "UPDATE", module = "DOCUMENT", description = "Admin updated document status")
    public ResponseEntity<String> updateDocumentStatus(
            @PathVariable Long userId,
            @RequestBody DocumentStatusUpdateDto dto
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getCnicStatus() != null) {
            user.setCnicStatus(dto.getCnicStatus());
        }
        if (dto.getLicenseStatus() != null) {
            user.setLicenseStatus(dto.getLicenseStatus());
        }
        if (dto.getVehicleDocStatus() != null) {
            user.setVehicleDocStatus(dto.getVehicleDocStatus());
        }
        if (dto.getReason() != null) {
            user.setDocumentRejectionReason(dto.getReason());
        }

        userRepository.save(user);

        return ResponseEntity.ok("Document status updated successfully");
    }
}