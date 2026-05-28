package com.travelpartner.rideplan.service.impl;

import com.travelpartner.rideplan.dto.GoogleDistanceMatrixResponse;
import com.travelpartner.rideplan.service.GoogleMapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleMapServiceImpl implements GoogleMapService {

    @Value("${google.maps.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public GoogleDistanceMatrixResponse.Element getDistanceAndTime(
            Double pickupLat,
            Double pickupLng,
            Double dropLat,
            Double dropLng
    ) {

        try {

            String url =
                    "https://maps.googleapis.com/maps/api/distancematrix/json" +
                            "?origins=" + pickupLat + "," + pickupLng +
                            "&destinations=" + dropLat + "," + dropLng +
                            "&mode=driving" +
                            "&units=metric" +
                            "&key=" + apiKey;

            log.info("Google Distance Matrix URL: {}", url);

            GoogleDistanceMatrixResponse response =
                    restTemplate.getForObject(
                            url,
                            GoogleDistanceMatrixResponse.class
                    );

            if (response == null) {
                log.error("Google response is null");
                return null;
            }

            log.info("Google API Response Status: {}", response.getStatus());

            if (!"OK".equals(response.getStatus())) {
                log.error("Google API Error Status: {}", response.getStatus());
                return null;
            }

            if (response.getRows() == null ||
                    response.getRows().isEmpty()) {

                log.error("Rows are empty");
                return null;
            }

            GoogleDistanceMatrixResponse.Element element =
                    response.getRows()
                            .get(0)
                            .getElements()
                            .get(0);

            log.info("Element Status: {}", element.getStatus());

            if (!"OK".equals(element.getStatus())) {
                log.error("Element status invalid: {}", element.getStatus());
                return null;
            }

            log.info(
                    "Distance: {}, Duration: {}",
                    element.getDistance().getText(),
                    element.getDuration().getText()
            );

            return element;

        } catch (Exception ex) {

            log.error("Error calling Google Distance Matrix API", ex);

            return null;
        }
    }
}