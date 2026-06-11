package com.praveen.simulator.model;


import com.praveen.simulator.entity.FlightManifest;
import com.praveen.simulator.entity.Passenger;

public record PnrRecord(
        String pnrLocator,                 // The 6-character booking reference (e.g., "X7Y2ZB")
        Passenger passengers,   // Who is traveling
        FlightManifest itinerary,        // Flight segments booked
        String ticketingStatus,            // e.g., "ISSUED", "PENDING"
        String bookingChannel,             // e.g., "WEB", "AGENCY"
        String transactionId,
        double totalAmountPaid,
        String currency
) {
}
