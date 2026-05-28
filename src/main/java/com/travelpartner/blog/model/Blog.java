package com.travelpartner.blog.model;

import com.travelpartner.blog.enums.BlogStatus;
import com.travelpartner.blogcategory.model.BlogCategory;
import com.travelpartner.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "blogs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Blog extends BaseEntity {

    private String coverImage;
    private String mainTitle;

    @Column(columnDefinition = "TEXT")
    private String description1;

    @Column(columnDefinition = "TEXT")
    private String description2;

    private LocalDate date;
    private String author;
    private String readTime;

    @ElementCollection
    private java.util.List<String> tags;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private BlogCategory category;

    private String seoTitle;
    private String seoDescription;
    @Enumerated(EnumType.STRING)
    private BlogStatus status;

    private Long views = 0L;

    private Boolean isDeleted = false;
    private LocalDateTime deletedAt;
}