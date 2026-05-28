package com.travelpartner.rideplan.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RidePlanProcessorFactory {

    private final List<RidePlanProcessor> processors;

    public RidePlanProcessor getProcessor(String module) {

        return processors.stream()
                .filter(p -> p.supportedModule().equalsIgnoreCase(module))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No processor found for module: " + module
                        )
                );
    }
}