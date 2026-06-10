package com.praveen.simulator;



import com.praveen.simulator.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AviationPipeline {

    // Stage 1: Map raw profile & flight inputs into an official PNR Record
    public PnrRecord createReservation(PassengerRecord passenger, FlightInfo flight) {
        String randomPnr = generatePnrLocator();

        return new PnrRecord(
                randomPnr,
                passenger,
                flight,
                "ISSUED",
                "WEB",
                   "1234",
                749.50,
                "USD"
        );
    }

    // Stage 2: Map PNR + Identity details into a Border Security Record (APP / APIS)
    public AppRecord processImmigrationClearance(PnrRecord pnr, String passportNum, String countryCode, String gender) {
        // Real-world systems look up passenger details by matching names from the PNR
        PassengerRecord traveler = pnr.passengers();

        // Run automated routing security logic
        String governmentResponse = "CLEARED";
        if (passportNum.startsWith("X") || countryCode.equals("REJ")) {
            governmentResponse = "MANUAL_CHECK";
        }

        return new AppRecord(
                pnr.pnrLocator(),
                passportNum,
                countryCode.toUpperCase(),
                LocalDate.now().plusYears(5), // Passport valid for 5 years
                LocalDate.of(1992, 6, 15),    // Simulated Date of Birth
                gender,
                countryCode.toUpperCase(),    // Nationality
                governmentResponse
        );
    }

    // Stage 3: Map PNR + APP status into an Airport Operation Control state (DCS Record)
    public DcsRecord performAirportCheckIn(PnrRecord pnr, AppRecord app, FlightInfo flight, String targetSeat, double bagWeight) {
        // Fail check-in instantly if border control flagged immigration profile
        if (app.clearanceStatus().equals("REJECTED")) {
            throw new IllegalStateException("Security Denied: DCS check-in blocked by border control protocol.");
        }

        String generatedTicket = "016" + (long)(Math.random() * 10000000000L); // Standard 13-digit ticket string
        String seqNumber = String.format("%03d", (int)(Math.random() * 150) + 1);

        // Map baggage elements
        List<BaggageInfo> bags = new ArrayList<>();
        if (bagWeight > 0) {
            String bagBarcode = flight.carrierCode() + (int)(Math.random() * 900000 + 100000);
            bags.add(new BaggageInfo(bagBarcode, bagWeight, flight.arrivalAirport()));
        }

        return new DcsRecord(
                generatedTicket,
                pnr.pnrLocator(),
                flight,
                targetSeat,
                seqNumber,
                "CHECKED_IN",
                bags,
                LocalDateTime.now(),
                pnr.totalAmountPaid() > 500 // Automatically trigger upgrade eligibility for premium segments
        );
    }

    private String generatePnrLocator() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(characters.charAt((int) (Math.random() * characters.length())));
        }
        return sb.toString();
    }
}