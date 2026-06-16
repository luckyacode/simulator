package com.praveen.simulator.helper;


import com.praveen.simulator.dto.*;
import com.praveen.simulator.entity.CheckInResponse;
import com.praveen.simulator.entity.PNR;
import com.praveen.simulator.entity.Passenger;
import com.praveen.simulator.kafka.KafkaService;
import com.praveen.simulator.kafka.events.CheckInEvent;
import com.praveen.simulator.service.CheckInResponseService;
import com.praveen.simulator.service.PassengerService;
import com.praveen.simulator.service.PnrService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AviationOrchestration {

    private final KafkaService kafkaService;
    private final CheckInResponseService checkInResponseService;
    private final PassengerService passengerService;
    private final PnrService pnrService;
    private final CommonMapper commonMapper;

    /**
     * Creates local booking data and fires off the asynchronous PNR generation pipeline via Kafka.
     */
    public PnrRequest createReservation(Passenger passenger, String flightId, double amount) {
        String randomPnr = Utils.generatePnrLocator();

        PnrRequest pnrRequest = PnrRequest.builder()
                .pnrId(randomPnr)
                .bookingDateTime(LocalDateTime.now())
                .bookingStatus("CONFIRMED")
                .flightId(flightId)
                .passengerId(passenger.getId())
                .ticketStatus(TicketStatus.CONFIRM)
                .bookingChannel(Channel.AIRLINE)
                .bookingClass(BookingClass.J)
                .agencyId("AGENT-01")
                .transactionId(UUID.randomUUID().toString())
                .totalAmount(amount)
                .currency("INR")
                .build();

        PnrEvent pnrEvent = commonMapper.toPnrEvent(pnrRequest);

        log.info("Publishing PNR Event to Kafka for PNR: {}, Transaction: {}", randomPnr, pnrRequest.getTransactionId());
        kafkaService.sendPNRMessage(pnrEvent);

        return pnrRequest;
    }

    /**
     * Orchestrates airport check-in. Updates documents and dispatches a Kafka message.
     * Fixed: Added compile-safe matching parameters from BookingService.
     * Fixed: Handled Optional PNR fetching defensively to avoid NullPointerExceptions.
     */
    public void performAirportCheckIn(CheckInEvent checkInEvent) {
        log.info("Processing airport check-in orchestrator for PNR: {}", checkInEvent.pnrId());

        // Securely fetch the PNR profile or fail gracefully if it doesn't exist
        PNR pnr = pnrService.getOptionalPnrById(checkInEvent.pnrId())
                .orElseThrow(() -> AirlineException.badRequest("Check-in failed: PNR locator not found."));



        Passenger passenger = pnr.getPassenger();
        if (passenger == null) {
            throw AirlineException.badRequest("Check-in failed: No passenger profile attached to PNR.");
        }

        // Mutate and save updated travel documentation (Passport/Visa info)
        passenger.setDocumentDetails(checkInEvent.documentDetails());
        passengerService.update(passenger);
        log.info("Passenger profile document data updated for ID: {}", passenger.getId());

        log.info("Dispatching async check-in transaction to Kafka with clearance reference: {}", checkInEvent.clearanceId());
        kafkaService.checkInOnAirport(checkInEvent);
    }

    /**
     * Polls/Fetches async check-in response status via government clearance ID.
     * Fixed: Eliminated @SneakyThrows, handled clean domain exception.
     */
    public CheckInResponse performAirportCheckInResponseByClearance(String clearanceId) {
        return checkInResponseService.getCheckInResponseByClearanceId(clearanceId)
                .orElseThrow(() -> AirlineException.badRequest("Check-in processing is still pending with border control authorities."));
    }

    /**
     * Polls/Fetches async check-in response status via passenger ID.
     * Fixed: Eliminated @SneakyThrows, handled clean domain exception.
     */
    public CheckInResponse performAirportCheckInResponseByPassenger(String passengerId) {
        return checkInResponseService.getCheckInResponseByPassengerId(passengerId)
                .orElseThrow(() -> AirlineException.badRequest("No active check-in transaction found for Passenger ID."));
    }
}