package com.travelpartner.rideplan.processor.impl;

import com.travelpartner.rideplan.dto.RidePlanDto;
import com.travelpartner.rideplan.model.RidePlan;
import com.travelpartner.rideplan.processor.RidePlanProcessor;
import org.springframework.stereotype.Component;

@Component
public class DeliveryRideProcessor implements RidePlanProcessor {

    @Override
    public void validate(RidePlanDto dto) {

        if (dto.getStuff() == null) {
            throw new IllegalArgumentException("Stuff is required");
        }

        if (dto.getBagsCount() == null) {
            throw new IllegalArgumentException("Bags count is required");
        }
    }

    @Override
    public void process(RidePlanDto dto, RidePlan ridePlan) {

        // delivery specific logic
    }

    @Override
    public String supportedModule() {
        return "DELIVERY";
    }
}