package com.praveen.simulator.service;

import com.praveen.simulator.dto.AppRequest;
import com.praveen.simulator.dto.PNRRequest;
import com.praveen.simulator.dto.PassengerRequest;
import com.praveen.simulator.entity.FlightManifest;
import com.praveen.simulator.entity.Passenger;
import com.praveen.simulator.helper.AviationOrchestration;
import com.praveen.simulator.model.*;
import com.praveen.simulator.model.FlightDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final AviationOrchestration aviationOrchestration;
    private final FlightService flightService;
    private final PassengerService passengerService;


    public PNRRequest createPassengerBooking(PassengerRequest passengerRequest, String flightId, double amount) {
        log.info("Booking Service processing for Passenger Booking ....");
        FlightManifest flightManifest = flightService.getFlightByFlightId(flightId);
        Passenger passenger = passengerService.addPassenger(passengerRequest);
        return aviationOrchestration.createReservation(passenger,flightManifest,amount);
    }

    public AppRecord processAPPData(PnrRecord pnr, String passportNum, String countryCode, String gender) {
        log.info("Booking Service processing for APP Data ....");
        return aviationOrchestration.processImmigrationClearance(pnr,passportNum,countryCode,gender);
    }

    public DcsRecord processDCSData(AppRequest app,  String targetSeat, double bagWeight) {
        log.info("Booking Service processing for DCS Data ....");
        return aviationOrchestration.performAirportCheckIn(app,targetSeat,bagWeight);
    }
}
