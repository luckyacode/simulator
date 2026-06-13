//package com.praveen.simulator.helper;
//
//import com.praveen.simulator.dto.AuthorityDirection;
//import com.praveen.simulator.entity.APP;
//import com.praveen.simulator.entity.FlightManifest;
//import com.praveen.simulator.entity.GovernmentClearanceResponse;
//import com.praveen.simulator.entity.Passenger;
//import com.praveen.simulator.model.AppRecord;
//import com.praveen.simulator.model.PnrRecord;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Optional;
//
//public class EdifactRecordConverter {
//
//    public static String toPnrGovEdifact(PnrRecord pnr, AppRecord app) {
//        DateTimeFormatter edifactDateFormat = DateTimeFormatter.ofPattern("ddMMyy");
//        DateTimeFormatter edifactTimeFormat = DateTimeFormatter.ofPattern("HHmm");
//
//        // Grab values cleanly using new record accessors
//        FlightManifest primaryFlight = pnr.itinerary();
//        Passenger mainPassenger = pnr.passengers();
//
//        StringBuilder sb = new StringBuilder();
//
//        // UNB & UNH Control Lines
//        sb.append("UNB+IATB:1+AIRLINE-APP+GDS-CORE+260610:1120+MSG01'\n");
//        sb.append("UNH+1+PNRGOV:UN:D:11B:IATA'\n");
//
//        // Map PNR Data
//        sb.append(String.format("ORG+1A+SYSTEM+++%s'\n", pnr.pnrLocator()));
//        sb.append(String.format("TVL+%s:%s+%s+%s+%s++%s%s'\n",
//                primaryFlight.getDepartureTime().format(edifactDateFormat),
//                primaryFlight.getDepartureTime().format(edifactTimeFormat),
//                primaryFlight.getDepartureAirport(),
//                primaryFlight.getArrivalAirport(),
//                primaryFlight.getFlightId(),
//                primaryFlight.getAirline(), primaryFlight.getFlightId()
//        ));
//
//        // Map Passenger Data
//        sb.append(String.format("TIF+%s+%s'\n", mainPassenger.getLastName().toUpperCase(), mainPassenger.getFirstName().toUpperCase()));
//        sb.append(String.format("NAT+2+%s'\n", app.nationality()));
//
//        // Map APIS / Secure Flight Record details
//        sb.append(String.format("DOC+P+%s+%s+%s'\n", app.issuingCountry(), app.passportNumber(), app.clearanceStatus()));
//
//        sb.append("UNT+8+1'\n");
//        sb.append("UNZ+1+MSG01'");
//
//        return sb.toString();
//    }
//
//    public static String convertToAppEdifact(APP payload) {
//        if (payload == null) {
//            return "";
//        }
//
//        StringBuilder edi = new StringBuilder();
//
//        // 1. Safe extraction of nested Government Clearances to avoid NullPointerExceptions
//        var govClearance = Optional.ofNullable(payload.getGovernmentClearanceResponse());
//        String clearanceId = govClearance.map(c -> c.getClearanceId().replace("-", "")).orElse("UNKNOWNID");
//        String directive = govClearance.map(GovernmentClearanceResponse::getAuthorityDirective).orElse(AuthorityDirection.DNL).toString();
//        String respCode = govClearance.map(GovernmentClearanceResponse::getResponseCode).orElse("99");
//
//        // 2. Format timestamps safely (Extract from createdDateTime string)
//        LocalDateTime baseTime = Optional.ofNullable(payload.getCreatedDateTime()).orElse(LocalDateTime.now());
//        String ediDate = baseTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
//        String interchangeDate = baseTime.format(DateTimeFormatter.ofPattern("yyMMdd:HHmm"));
//
//        // 3. Track Segment Counter dynamically (starts at 0, incremented for every payload line)
//        int segmentCount = 0;
//
//        // --- Outer Interchange Envelope Header ---
//        edi.append("UNB+UNOA:2+AGENCYID+GOV-BORDER+")
//                .append(interchangeDate).append("+")
//                .append(payload.getPnrId()).append("'");
//
//        // --- Message Body ---
//        edi.append("UNH+1+PAXRST:D:02B:UN'"); segmentCount++;
//        edi.append("BGM+9+").append(clearanceId).append("+9'"); segmentCount++;
//        edi.append("DTM+137:").append(ediDate).append(":203'"); segmentCount++;
//        edi.append("RFF+SND:").append(payload.getPnrId()).append("'"); segmentCount++;
//
//        // Flight & Port routing segments
//        edi.append("TBDT+").append(payload.getFlightId()).append("'"); segmentCount++;
//        edi.append("LOC+5+").append(payload.getDeparturePort()).append("'"); segmentCount++;
//        edi.append("LOC+8+").append(payload.getArrivalPort()).append("'"); segmentCount++;
//
//        // Passport document segment snapshot
//        if (payload.getPassportNumber() != null) {
//            edi.append("DOC+9+").append(payload.getPassportNumber()).append("'"); segmentCount++;
//        }
//
//        // Government Clearance Status mapping
//        edi.append("ATT+2+").append(directive).append("+").append(respCode).append("'"); segmentCount++;
//
//        // --- Message Control Footers ---
//        segmentCount++; // Accounting for the UNT segment itself
//        edi.append("UNT+").append(segmentCount).append("+1'");
//
//        // --- Outer Interchange Envelope Trailer ---
//        edi.append("UNZ+1+").append(payload.getPnrId()).append("'");
//
//        return edi.toString();
//    }
//}