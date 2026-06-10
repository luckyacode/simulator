package com.praveen.simulator;

import com.praveen.simulator.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final AviationOrchestration aviationOrchestration;


    public PnrRecord createPassengerBooking(PassengerRecord passengerRecord, FlightInfo flightInfo, double amount) {
        log.info("Booking Service processing for Passenger Booking ....");
        return aviationOrchestration.createReservation(passengerRecord,flightInfo,amount);
    }

    public AppRecord processAPPData(PnrRecord pnr, String passportNum, String countryCode, String gender) {
        log.info("Booking Service processing for APP Data ....");
        return aviationOrchestration.processImmigrationClearance(pnr,passportNum,countryCode,gender);
    }

    public DcsRecord processDCSData(PnrRecord pnr, AppRecord app, FlightInfo flight, String targetSeat, double bagWeight) {
        log.info("Booking Service processing for DCS Data ....");
        return aviationOrchestration.performAirportCheckIn(pnr,app,flight,targetSeat,bagWeight);
    }
}
