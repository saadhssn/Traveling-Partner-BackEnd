package com.travelpartner.rideplan.service;

import com.travelpartner.rideplan.dto.GoogleDistanceMatrixResponse;

public interface GoogleMapService {

    GoogleDistanceMatrixResponse.Element getDistanceAndTime(
            Double pickupLat,
            Double pickupLng,
            Double dropLat,
            Double dropLng
    );
}