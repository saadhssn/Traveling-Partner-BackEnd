package com.travelpartner.color.dto;

import com.travelpartner.color.enums.ColorStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColorDto {

    private Long id;

    private String name;

    private String image;

    private ColorStatus status;
}