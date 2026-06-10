package com.praveen.simulator.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FlightRequest {
    private String airline;
    private String sourceAirport;
    private String departureCountry;
    private String destAirport;
    private String arrivalCountry;
    private String equipment;
}
