package com.travelpartner.color.controller;

import com.travelpartner.audit.annotation.AuditAction;
import com.travelpartner.color.dto.ColorDto;
import com.travelpartner.color.service.ColorService;
import com.travelpartner.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/colors")
@RequiredArgsConstructor
public class ColorController {

    private final ColorService service;

    @PostMapping("/create")
    @AuditAction(action = "CREATE", module = "COLOR", description = "Create color")
    public ApiResponse<ColorDto> create(@Valid @RequestBody ColorDto dto) {
        return service.create(dto);
    }

    @GetMapping("/getAll")
    public ApiResponse<Page<ColorDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {

        if (search == null || search.isEmpty()) {
            return service.getAll(page, size);
        }

        return service.getAllFiltered(page, size, search);
    }

    @GetMapping("/getById/{id}")
    @AuditAction(action = "READ", module = "COLOR", description = "Get color by id")
    public ApiResponse<ColorDto> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/update/{id}")
    @AuditAction(action = "UPDATE", module = "COLOR", description = "Update color")
    public ApiResponse<ColorDto> update(@PathVariable Long id,
                                        @Valid @RequestBody ColorDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    @AuditAction(action = "DELETE", module = "COLOR", description = "Delete color")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        return service.delete(id);
    }
}