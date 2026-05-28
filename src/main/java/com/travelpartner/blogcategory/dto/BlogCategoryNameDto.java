package com.travelpartner.blogcategory.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogCategoryNameDto {
    private Long id;
    private String name;
}