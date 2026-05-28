package com.travelpartner.rideplan.processor.impl;

import com.travelpartner.rideplan.dto.RidePlanDto;
import com.travelpartner.rideplan.model.RidePlan;
import com.travelpartner.rideplan.processor.RidePlanProcessor;
import org.springframework.stereotype.Component;

@Component
public class PoolRideProcessor implements RidePlanProcessor {

    @Override
    public void validate(RidePlanDto dto) {

        if (dto.getSeats() == null || dto.getSeats() <= 0) {
            throw new IllegalArgumentException("Seats are required");
        }

        if (dto.getSeatsAvailable() == null) {
            throw new IllegalArgumentException("Seats available required");
        }
    }

    @Override
    public void process(RidePlanDto dto, RidePlan ridePlan) {

        if (ridePlan.getSeatsReserved() == null) {
            ridePlan.setSeatsReserved(0);
        }
    }

    @Override
    public String supportedModule() {
        return "POOL_RIDE";
    }
}