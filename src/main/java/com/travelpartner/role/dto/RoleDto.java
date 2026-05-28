package com.travelpartner.role.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDto {

    private Long id;

    @NotBlank(message = "Role name is required")
    private String name;

    @NotBlank(message = "Role slug is required")
    private String slug;
}