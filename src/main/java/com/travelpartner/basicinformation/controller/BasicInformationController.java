package com.travelpartner.basicinformation.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.basicinformation.dto.BasicInformationDto;
import com.travelpartner.basicinformation.service.BasicInformationService;
import com.travelpartner.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/basicInformation")
@RequiredArgsConstructor
public class BasicInformationController {

    private final BasicInformationService service;

    @PostMapping("/create")
    @AuditAction(action = "CREATE", module = "BASIC_INFORMATION", description = "Create basic information")
    public ApiResponse<BasicInformationDto> create(@Valid @RequestBody BasicInformationDto dto) {
        return service.create(dto);
    }

    @GetMapping("/getAll")
    @AuditAction(action = "READ", module = "BASIC_INFORMATION", description = "Get all basic information")
    public ApiResponse<Page<BasicInformationDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Boolean deleted) {

        if (deleted == null) {
            return service.getAll(page, size);
        } else {
            return service.getAllFiltered(page, size, deleted);
        }
    }

    @GetMapping("/getById/{id}")
    @AuditAction(action = "READ", module = "BASIC_INFORMATION", description = "Get basic information by id")
    public ApiResponse<BasicInformationDto> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/update/{id}")
    @AuditAction(action = "UPDATE", module = "BASIC_INFORMATION", description = "Update basic information")
    public ApiResponse<BasicInformationDto> update(@PathVariable Long id,
                                                   @Valid @RequestBody BasicInformationDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    @AuditAction(action = "DELETE", module = "BASIC_INFORMATION", description = "Delete basic information")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
