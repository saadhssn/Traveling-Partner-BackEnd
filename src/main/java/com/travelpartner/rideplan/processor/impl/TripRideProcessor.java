package com.travelpartner.rideplan.processor.impl;

import com.travelpartner.rideplan.dto.RidePlanDto;
import com.travelpartner.rideplan.model.RidePlan;
import com.travelpartner.rideplan.processor.RidePlanProcessor;
import org.springframework.stereotype.Component;

@Component
public class TripRideProcessor implements RidePlanProcessor {

    @Override
    public void validate(RidePlanDto dto) {

        if (dto.getTourDays() == null) {
            throw new IllegalArgumentException("Tour days required");
        }

        if (dto.getVisitingPoints() == null) {
            throw new IllegalArgumentException("Visiting points required");
        }
    }

    @Override
    public void process(RidePlanDto dto, RidePlan ridePlan) {

    }

    @Override
    public String supportedModule() {
        return "TRIP";
    }
}