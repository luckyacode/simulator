package com.praveen.simulator.helper;

import com.praveen.simulator.dto.AuthorityDirection;
import com.praveen.simulator.dto.PassengerRequest;
import com.praveen.simulator.entity.APP;
import com.praveen.simulator.entity.GovernmentClearanceResponse;
import com.praveen.simulator.model.FlightDetail;
import lombok.SneakyThrows;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Utils {
    private static ObjectMapper objectMapper = new ObjectMapper();
    public static List<PassengerRequest> mapPassenger(List<String> list){
        return list.stream().map(line -> line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"))
                .map(PassengerRequest::new).toList();
    }

    public static List<FlightDetail> mapFlight(List<String> list){
        return list.stream().map(line -> line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"))
                .map(FlightDetail::new).toList();
    }

    @SneakyThrows
    public static List<PassengerRequest> fetchPassengerFromFile(int size){
        Scanner scan = new Scanner(new File("files/people.csv"));
        List<String> list = new ArrayList<>();
        scan.nextLine();
        while(scan.hasNext()){
            if(list.size()==size)
                break;
            list.add(scan.nextLine());
        }
        return mapPassenger(list);
    }

    @SneakyThrows
    public static List<FlightDetail> fetchFlightFromFile(int size){
//        Scanner scan = new Scanner(new File("files/international_flight_traffic.csv"));
        Scanner scan = new Scanner(new File("files/international_flight_traffic_updated.csv"));
        List<String> list = new ArrayList<>();
        scan.nextLine();
        while(scan.hasNext()){
            if(list.size()==size)
                break;
            list.add(scan.nextLine());
        }
        return mapFlight(list);
    }

    public static String generatePnrLocator() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(characters.charAt((int) (Math.random() * characters.length())));
        }
        return sb.toString();
    }

    public static<T> String objectToJson(T object){
        return objectMapper.writeValueAsString(object);
    }

    public static<T> T jsonToObject(String json,Class<T> targetClass){
        return objectMapper.readValue(json,targetClass);
    }


    public static String generateUniqueId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String convertToAppEdifact(APP payload) {
        if (payload == null) {
            return "";
        }

        StringBuilder edi = new StringBuilder();

        // 1. Safe extraction of nested Government Clearances to avoid NullPointerExceptions
        var govClearance = Optional.ofNullable(payload.getGovernmentClearanceResponse());
        String clearanceId = govClearance.map(c -> c.getClearanceId().replace("-", "")).orElse("UNKNOWNID");
        String directive = govClearance.map(GovernmentClearanceResponse::getAuthorityDirective).orElse(AuthorityDirection.DNL).toString();
        String respCode = govClearance.map(GovernmentClearanceResponse::getResponseCode).orElse("99");

        // 2. Format timestamps safely (Extract from createdDateTime string)
        LocalDateTime baseTime = Optional.ofNullable(payload.getCreatedDateTime()).orElse(LocalDateTime.now());
        String ediDate = baseTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        String interchangeDate = baseTime.format(DateTimeFormatter.ofPattern("yyMMdd:HHmm"));

        // 3. Track Segment Counter dynamically (starts at 0, incremented for every payload line)
        int segmentCount = 0;

        // --- Outer Interchange Envelope Header ---
        edi.append("UNB+UNOA:2+AGENCYID+GOV-BORDER+")
                .append(interchangeDate).append("+")
                .append(payload.getPnrId()).append("'");

        // --- Message Body ---
        edi.append("UNH+1+PAXRST:D:02B:UN'"); segmentCount++;
        edi.append("BGM+9+").append(clearanceId).append("+9'"); segmentCount++;
        edi.append("DTM+137:").append(ediDate).append(":203'"); segmentCount++;
        edi.append("RFF+SND:").append(payload.getPnrId()).append("'"); segmentCount++;

        // Flight & Port routing segments
        edi.append("TBDT+").append(payload.getFlightId()).append("'"); segmentCount++;
        edi.append("LOC+5+").append(payload.getDeparturePort()).append("'"); segmentCount++;
        edi.append("LOC+8+").append(payload.getArrivalPort()).append("'"); segmentCount++;

        // Passport document segment snapshot
        if (payload.getPassportNumber() != null) {
            edi.append("DOC+9+").append(payload.getPassportNumber()).append("'"); segmentCount++;
        }

        // Government Clearance Status mapping
        edi.append("ATT+2+").append(directive).append("+").append(respCode).append("'"); segmentCount++;

        // --- Message Control Footers ---
        segmentCount++; // Accounting for the UNT segment itself
        edi.append("UNT+").append(segmentCount).append("+1'");

        // --- Outer Interchange Envelope Trailer ---
        edi.append("UNZ+1+").append(payload.getPnrId()).append("'");

        return edi.toString();
    }

}
