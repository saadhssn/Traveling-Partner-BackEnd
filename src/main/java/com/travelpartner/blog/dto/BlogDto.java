package com.travelpartner.blog.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogDto {

    private Long id;
    private String coverImage;
    private String mainTitle;
    private String description1;
    private String description2;
    private LocalDate date;
    private String author;
    private String readTime;
    private List<String> tags;

    private String seoTitle;
    private String seoDescription;
    private String status; // keep string for API (no breaking change)
    private Long views;

    private Long categoryId;
    private String categoryName;

}