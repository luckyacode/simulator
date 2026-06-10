package com.praveen.simulator.model;


public record PnrRecord(
        String pnrLocator,                 // The 6-character booking reference (e.g., "X7Y2ZB")
        PassengerRecord passengers,   // Who is traveling
        FlightDetail itinerary,        // Flight segments booked
        String ticketingStatus,            // e.g., "ISSUED", "PENDING"
        String bookingChannel,             // e.g., "WEB", "AGENCY"
        String transactionId,
        double totalAmountPaid,
        String currency
) {
}
