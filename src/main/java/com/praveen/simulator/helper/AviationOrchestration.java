package com.praveen.simulator.helper;


import com.praveen.simulator.dto.*;
import com.praveen.simulator.entity.FlightManifest;
import com.praveen.simulator.entity.Passenger;
import com.praveen.simulator.kafka.KafkaPublisher;
import com.praveen.simulator.kafka.KafkaService;
import com.praveen.simulator.model.*;
import com.praveen.simulator.model.FlightDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AviationOrchestration {
    private final KafkaService kafkaService;

    public PNRRequest createReservation(Passenger passenger, String flightId, double amount) {
        String randomPnr = Utils.generatePnrLocator();
        PNRRequest pnrRequest = PNRRequest.builder().
                PNRId(randomPnr).bookingDateTime(LocalDateTime.now()).bookingStatus("CONFIRMED").flightId(flightId).passengerId(passenger.getId()).ticketStatus(TicketStatus.CONFIRM).bookingChannel(Channel.AIRLINE).bookingClass(BookingClass.J).agencyId("AGENT-01").transactionId(UUID.randomUUID().toString()).totalAmount(amount).currency("INR").build();
        log.info("PNR request created for : {}",pnrRequest);
        log.info("Publishing message for PNRRequest ...");
        kafkaService.sendPNRMessage(pnrRequest);
        return pnrRequest;
    }

    public AppRecord processImmigrationClearance(PnrRecord pnr, String passportNum, String countryCode, String gender) {
        Passenger traveler = pnr.passengers();
        String governmentResponse = "CLEARED";
        if (passportNum.startsWith("X") || countryCode.equals("REJ")) {
            governmentResponse = "MANUAL_CHECK";
        }

        return new AppRecord(pnr.pnrLocator(), passportNum, countryCode.toUpperCase(), LocalDate.now().plusYears(5), // Passport valid for 5 years
                LocalDate.of(1992, 6, 15),    // Simulated Date of Birth
                gender, countryCode.toUpperCase(),    // Nationality
                governmentResponse);
    }

    public DcsRecord performAirportCheckIn(AppRequest app, String targetSeat, double bagWeight) {
        FlightManifest flight = app.pnr().itinerary();
        // Fail check-in instantly if border control flagged immigration profile
//        if (app.clearanceStatus().equals("REJECTED")) {
//            throw new IllegalStateException("Security Denied: DCS check-in blocked by border control protocol.");
//        }

        String generatedTicket = "016" + (long) (Math.random() * 10000000000L); // Standard 13-digit ticket string
        String seqNumber = String.format("%03d", (int) (Math.random() * 150) + 1);

        List<BaggageInfo> bags = new ArrayList<>();
        if (bagWeight > 0) {
            String bagBarcode = flight.getFlightId() + (int) (Math.random() * 900000 + 100000);
            bags.add(new BaggageInfo(bagBarcode, bagWeight, flight.getArrivalAirport()));
        }

        return new DcsRecord(generatedTicket, app.pnr().pnrLocator(), flight, targetSeat, seqNumber, "CHECKED_IN", bags, LocalDateTime.now(), app.pnr().totalAmountPaid() > 500 // Automatically trigger upgrade eligibility for premium segments
        );
    }

}