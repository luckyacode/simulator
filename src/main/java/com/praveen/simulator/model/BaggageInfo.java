package com.praveen.simulator.model;

// Sub-record for DCS Baggage Tracking
public record BaggageInfo(
        String tagNumber,                  // Barcode tag (e.g., "UA123456")
        double weightInKg,
        String destination
) {
}
