package com.travelpartner.rideplan.processor;

import com.travelpartner.rideplan.dto.RidePlanDto;
import com.travelpartner.rideplan.model.RidePlan;

public interface RidePlanProcessor {

    void validate(RidePlanDto dto);

    void process(RidePlanDto dto, RidePlan ridePlan);

    String supportedModule();
}