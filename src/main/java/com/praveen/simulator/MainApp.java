package com.praveen.simulator;

import com.praveen.simulator.helper.AviationPipeline;
import com.praveen.simulator.helper.EdifactRecordConverter;
import com.praveen.simulator.model.*;
import com.praveen.simulator.model.FlightDetail;

import java.time.LocalDateTime;

public class MainApp {
    public static void main(String[] args) {
        AviationPipeline pipeline = new AviationPipeline();

        // Step 1: Create your core structural building blocks
        PassengerRecord passengerInput = new PassengerRecord("Chris", "Russell", "vhill@example.net", "633-913-4354");
        FlightDetail flightInput = new FlightDetail(new String[]{"2B", "849", "GYD", "NBC", LocalDateTime.now().plusDays(1).withHour(9).withMinute(15).toString()});

        System.out.println("====== STEP 1: INITIALIZING PNR RESERVATION ======");
        PnrRecord customerPnr = pipeline.createReservation(passengerInput, flightInput);
        System.out.println("Generated PNR Object: " + customerPnr);
        System.out.println("PNR Locator: " + customerPnr.pnrLocator() + " | Channel: " + customerPnr.bookingChannel());

        System.out.println("\n====== STEP 2: ENROLLING IN APP BORDER SECURITY ======");
        // Simulate passport entry verification
        AppRecord securityAppRecord = pipeline.processImmigrationClearance(customerPnr, "KUW994012A", "KWT", "M");
        System.out.println("Immigration Response: " + securityAppRecord.clearanceStatus() + " for Passport " + securityAppRecord.passportNumber());

        System.out.println("\n====== STEP 3: EXECUTING DCS AIRPORT CHECK-IN ======");
        DcsRecord airportDcsState = pipeline.performAirportCheckIn(customerPnr, securityAppRecord, flightInput, "12A", 23.5);
        System.out.println("Check-in Status: " + airportDcsState.passengerStatus());
        System.out.println("Assigned Seat: " + airportDcsState.seatNumber() + " | Boarding Sequence: " + airportDcsState.boardingSequenceNumber());
        System.out.println("Checked Bag Details: " + airportDcsState.checkedBaggage().get(0));
        System.out.println("Upgrade Status (Frequent Flyer Match): " + airportDcsState.isFrequentFlyerUpgrade());

        System.out.println("\n====== STEP 4: COMPILED TRANSLATION TO REAL WORLD EDIFACT ======");
        String rawTransmission = EdifactRecordConverter.toPnrGovEdifact(customerPnr, securityAppRecord);
        System.out.println(rawTransmission);
    }
}