package com.travelpartner.rideplan.processor.impl;

import com.travelpartner.rideplan.dto.RidePlanDto;
import com.travelpartner.rideplan.model.RidePlan;
import com.travelpartner.rideplan.processor.RidePlanProcessor;
import org.springframework.stereotype.Component;

@Component
public class TaxiStandRideProcessor implements RidePlanProcessor {

    @Override
    public void validate(RidePlanDto dto) {

        if (dto.getPickUpLocation() == null) {
            throw new IllegalArgumentException("Pickup location is required");
        }

        if (dto.getDropOffLocation() == null) {
            throw new IllegalArgumentException("Dropoff location is required");
        }
    }

    @Override
    public void process(RidePlanDto dto, RidePlan ridePlan) {

        // taxi stand specific logic
    }

    @Override
    public String supportedModule() {
        return "TAXI_STAND";
    }
}