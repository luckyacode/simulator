package com.praveen.simulator;

import com.praveen.simulator.model.AppRecord;
import com.praveen.simulator.model.FlightInfo;
import com.praveen.simulator.model.PassengerRecord;
import com.praveen.simulator.model.PnrRecord;

import java.time.format.DateTimeFormatter;

public class EdifactRecordConverter {

    public static String toPnrGovEdifact(PnrRecord pnr, AppRecord app) {
        DateTimeFormatter edifactDateFormat = DateTimeFormatter.ofPattern("ddMMyy");
        DateTimeFormatter edifactTimeFormat = DateTimeFormatter.ofPattern("HHmm");

        // Grab values cleanly using new record accessors
        FlightInfo primaryFlight = pnr.itinerary();
        PassengerRecord mainPassenger = pnr.passengers();

        StringBuilder sb = new StringBuilder();

        // UNB & UNH Control Lines
        sb.append("UNB+IATB:1+AIRLINE-APP+GDS-CORE+260610:1120+MSG01'\n");
        sb.append("UNH+1+PNRGOV:UN:D:11B:IATA'\n");

        // Map PNR Data
        sb.append(String.format("ORG+1A+SYSTEM+++%s'\n", pnr.pnrLocator()));
        sb.append(String.format("TVL+%s:%s+%s+%s+%s++%s%s'\n",
                primaryFlight.departureTime().format(edifactDateFormat),
                primaryFlight.departureTime().format(edifactTimeFormat),
                primaryFlight.departureAirport(),
                primaryFlight.arrivalAirport(),
                primaryFlight.carrierCode(),
                primaryFlight.carrierCode(), primaryFlight.flightNumber()
        ));

        // Map Passenger Data
        sb.append(String.format("TIF+%s+%s'\n", mainPassenger.lastName().toUpperCase(), mainPassenger.firstName().toUpperCase()));
        sb.append(String.format("NAT+2+%s'\n", app.nationality()));

        // Map APIS / Secure Flight Record details
        sb.append(String.format("DOC+P+%s+%s+%s'\n", app.issuingCountry(), app.passportNumber(), app.clearanceStatus()));

        sb.append("UNT+8+1'\n");
        sb.append("UNZ+1+MSG01'");

        return sb.toString();
    }
}