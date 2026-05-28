package com.travelpartner.banner.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BannerDto {

    private Long id;
    private String bannerImage;
    private String bannerTitle;
    private String bannerDescription;
}