package com.praveen.simulator.kafka.events;

import com.praveen.simulator.dto.DocumentDetails;
import com.praveen.simulator.entity.*;
import com.praveen.simulator.helper.CommonMapper;
import com.praveen.simulator.helper.Utils;
import com.praveen.simulator.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaHelper {
    private final PnrService pnrService;
    private final AppService appService;
    private final CommonMapper commonMapper;


    public void processAppMessage(PNR pnr, CheckInResponse checkInResponse) {
        FlightManifest flight = pnr.getFlight();
        if (appService.searchAppData(null, String.valueOf(pnr.getPassenger().getId())).isEmpty()) {
            APP app = APP.builder().appId(Utils.generateUniqueId()).pnrId(checkInResponse.getPnrId()).
                    flightId(flight.getFlightId()).departurePort(flight.getDepartureAirport()).
                    arrivalPort(flight.getArrivalAirport()).
                    passportNumber(Optional.ofNullable(pnr.getPassenger())
                            .map(Passenger::getDocumentDetails)
                            .map(DocumentDetails::getPassportNumber)
                            .orElse(null)).
                    createdDateTime(LocalDateTime.now()).governmentClearanceResponse(checkInResponse.getGovernmentClearanceResponse()).processingStatus(AppProcessingStatus.PROCESSED).build();
            appService.addAppData(app);
        } else {
            log.info(" Error while commiting to database APP Message is present with ID: {}", pnr.getPnrId());
        }
    }

    public DCSRequestEvent prepareDcsRequestEvent(PNR pnr) {
        log.info("Preparing DCS Request for PNR : {}", pnr.getPnrId());
        return DCSRequestEvent.builder().flightId(pnr.getFlight().getFlightId()).pnrId(pnr.getPnrId()).passengerId(String.valueOf(pnr.getPassenger().getId())).
                passengerName(pnr.getPassenger().getFullName()).build();
    }

}
