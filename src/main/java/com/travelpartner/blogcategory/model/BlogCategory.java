package com.travelpartner.blogcategory.model;

import com.travelpartner.common.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "blog_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogCategory extends BaseEntity {

    @NotBlank
    @Column(unique = true)
    private String name;

    private String description;

    private Boolean isDeleted = false;
    private LocalDateTime deletedAt;
}