package com.praveen.simulator.service;

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

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final AviationOrchestration aviationOrchestration;
    private final PassengerService passengerService;


    public PNRRequest createPassengerBooking(PassengerRequest passengerRequest, String flightId, double amount) {
        log.info("Booking Service processing for Passenger Booking ....");
        Passenger passenger = passengerService.addPassenger(passengerRequest);
        return aviationOrchestration.createReservation(passenger,flightId,amount);
    }

    public String checkInPassenger(CheckInRequest checkInRequest) {
        log.info("checkIn request initiated....{} ",checkInRequest);
        String clearanceId = Utils.generateUniqueId();
        checkInRequest.setClearanceId(clearanceId);
        aviationOrchestration.performAirportCheckIn(checkInRequest);
        return clearanceId;
    }

    public CheckInResponse checkInStatusByClearanceId(String clearanceId) {
        log.info("fetching checkIn Resposne for clearance {} ",clearanceId);
        return aviationOrchestration.performAirportCheckInResponseByClearance(clearanceId);
    }

    public CheckInResponse checkInStatusByPassengerId(String passengerId) {
        log.info("fetching checkIn Resposne for passenger {} ",passengerId);
        return aviationOrchestration.performAirportCheckInResponseByPassenger(passengerId);
    }
}
