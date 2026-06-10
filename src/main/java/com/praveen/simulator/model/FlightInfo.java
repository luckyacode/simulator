package com.praveen.simulator.model;

import java.time.LocalDateTime;

public record FlightInfo(String carrierCode,      // e.g., "AA", "LH"
                  String flightNumber,     // e.g., "123"
                  String departureAirport, // e.g., "JFK"
                  String arrivalAirport,   // e.g., "LAX"
                  LocalDateTime departureTime) {
}
