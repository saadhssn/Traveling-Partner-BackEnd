package com.travelpartner.rideplan.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GoogleDistanceMatrixResponse {

    private String status;

    private List<String> originAddresses;

    private List<String> destinationAddresses;

    private List<Row> rows;

    @Getter
    @Setter
    public static class Row {

        private List<Element> elements;
    }

    @Getter
    @Setter
    public static class Element {

        private Distance distance;

        private Duration duration;

        private String status;
    }

    @Getter
    @Setter
    public static class Distance {

        private String text;

        private Integer value;
    }

    @Getter
    @Setter
    public static class Duration {

        private String text;

        private Integer value;
    }
}