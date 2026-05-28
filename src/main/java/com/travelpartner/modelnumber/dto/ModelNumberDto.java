package com.travelpartner.modelnumber.dto;

import com.travelpartner.modelnumber.enums.ModelNumberStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModelNumberDto {

    private Long id;

    private String name;

    private String image;

    private ModelNumberStatus status;
}