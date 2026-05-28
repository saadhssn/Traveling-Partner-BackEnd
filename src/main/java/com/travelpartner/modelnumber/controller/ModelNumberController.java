package com.travelpartner.modelnumber.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.common.response.ApiResponse;
import com.travelpartner.modelnumber.dto.ModelNumberDto;
import com.travelpartner.modelnumber.service.ModelNumberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/modelNumbers")
@RequiredArgsConstructor
public class ModelNumberController {

    private final ModelNumberService service;

    @PostMapping("/create")
    @AuditAction(action = "CREATE", module = "MODEL_NUMBER", description = "Create model number")
    public ApiResponse<ModelNumberDto> create(@Valid @RequestBody ModelNumberDto dto) {
        return service.create(dto);
    }

    @GetMapping("/getAll")
    @AuditAction(action = "READ", module = "MODEL_NUMBER", description = "Get all model numbers")
    public ApiResponse<Page<ModelNumberDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {

        return service.getAllFiltered(page, size, search);
    }

    @GetMapping("/getById/{id}")
    @AuditAction(action = "READ", module = "MODEL_NUMBER", description = "Get model number by id")
    public ApiResponse<ModelNumberDto> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/update/{id}")
    @AuditAction(action = "UPDATE", module = "MODEL_NUMBER", description = "Update model number")
    public ApiResponse<ModelNumberDto> update(@PathVariable Long id,
                                              @Valid @RequestBody ModelNumberDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    @AuditAction(action = "DELETE", module = "MODEL_NUMBER", description = "Delete model number")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        return service.delete(id);
    }
}