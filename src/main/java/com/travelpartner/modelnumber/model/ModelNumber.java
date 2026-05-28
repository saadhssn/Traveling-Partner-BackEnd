package com.travelpartner.modelnumber.model;

import com.travelpartner.common.base.BaseEntity;
import com.travelpartner.modelnumber.enums.ModelNumberStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "model_numbers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModelNumber extends BaseEntity {

    private String name;

    private String image;

    @Enumerated(EnumType.STRING)
    private ModelNumberStatus status;
}