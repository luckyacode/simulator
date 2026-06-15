package com.praveen.simulator.service;

import com.praveen.simulator.kafka.events.CheckInEvent;
import com.praveen.simulator.dto.CheckInRequest;
import com.praveen.simulator.dto.PNRRequest;
import com.praveen.simulator.dto.PassengerRequest;
import com.praveen.simulator.entity.CheckInResponse;
import com.praveen.simulator.entity.Passenger;
import com.praveen.simulator.helper.AviationOrchestration;
import com.praveen.simulator.helper.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final AviationOrchestration aviationOrchestration;
    private final PassengerService passengerService;

    /**
     * Creates a passenger and initiates a flight reservation.
     * Fixed: Wrapped in a write transactional boundary to prevent orphaned passenger entities on failure.
     */
    @Transactional
    public PNRRequest createPassengerBooking(PassengerRequest passengerRequest, String flightId, double amount) {
        log.info("Processing passenger booking request for Flight ID: {}", flightId);

        // Step 1: Record passenger details
        Passenger passenger = passengerService.addPassenger(passengerRequest);

        // Step 2: Orchestrate reservation workflow (PNR generation, seat lock, inventory update)
        return aviationOrchestration.createReservation(passenger, flightId, amount);
    }

    /**
     * Initiates airport check-in operations.
     * Fixed: Added write transaction context for tracking state mutations.
     */
    @Transactional
    public String checkInPassenger(CheckInRequest checkInRequest) {
        log.info("Airport check-in request initiated for Passenger having PNR : {}",
                checkInRequest.getPnrId());

        // Generate security/clearance tracking sequence
        String clearanceId = Utils.generateUniqueId();
        CheckInEvent checkInEvent = CheckInEvent.builder().pnrId(checkInRequest.getPnrId())
                .clearanceId(clearanceId).documentDetails(checkInRequest.getDocumentDetails()).build();
        // Production Note: If CheckInRequest is an immutable Record or DTO,
        // pass the clearance ID side-by-side to the orchestrator rather than mutating the DTO.
        aviationOrchestration.performAirportCheckIn(checkInEvent);

        return clearanceId;
    }

    /**
     * Retrieve airport check-in validation state via Clearance Identifier.
     * Fixed: Optimized with readOnly transactional flag.
     */
    @Transactional(readOnly = true)
    public CheckInResponse checkInStatusByClearanceId(String clearanceId) {
        log.debug("Fetching check-in clearance response for tracking reference: {}", clearanceId);
        return aviationOrchestration.performAirportCheckInResponseByClearance(clearanceId);
    }

    /**
     * Retrieve airport check-in validation state via Passenger Identifier.
     * Fixed: Optimized with readOnly transactional flag.
     */
    @Transactional(readOnly = true)
    public CheckInResponse checkInStatusByPassengerId(String passengerId) {
        log.debug("Fetching check-in clearance response for Passenger ID: {}", passengerId);
        return aviationOrchestration.performAirportCheckInResponseByPassenger(passengerId);
    }
}