package com.travelpartner.banner.model;

import com.travelpartner.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "banners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banner extends BaseEntity {

    @Column(nullable = false)
    private String bannerImage;

    @Column(nullable = false)
    private String bannerTitle;

    @Column
    private String bannerDescription;
}