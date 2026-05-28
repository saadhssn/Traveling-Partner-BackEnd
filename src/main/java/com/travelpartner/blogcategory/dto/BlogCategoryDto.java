package com.travelpartner.blogcategory.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogCategoryDto {

    private Long id;

    private String name;

    private String description;
}