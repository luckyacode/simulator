package com.praveen.simulator.kafka;

import com.praveen.simulator.dto.DocumentDetails;
import com.praveen.simulator.entity.*;
import com.praveen.simulator.helper.CommonMapper;
import com.praveen.simulator.helper.Utils;
import com.praveen.simulator.kafka.events.DCSRequestEvent;
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
        if (pnr.getPassenger() == null) {
            log.error("APP execution skipped: Passenger profile data is not linked to PNR locator: {}", pnr.getPnrId());
            return;
        }
        String passengerId = String.valueOf(pnr.getPassenger().getId());
        FlightManifest flight = pnr.getFlight();
        String passportNumber = Optional.of(pnr.getPassenger())
                .map(Passenger::getDocumentDetails)
                .map(DocumentDetails::getPassportNumber)
                .orElse(null);
        if (appService.searchAppData(null, String.valueOf(pnr.getPassenger().getId())).isEmpty()) {
            APP app = APP.builder().
                    appId(Utils.generateUniqueId()).
                    pnrId(checkInResponse.getPnrId()).
                    flightId(flight.getFlightId()).
                    departurePort(flight.getDepartureAirport()).
                    arrivalPort(flight.getArrivalAirport()).
                    passportNumber(passportNumber).
                    createdDateTime(LocalDateTime.now()).
                    governmentClearanceResponse(checkInResponse.getGovernmentClearanceResponse()).
                    processingStatus(AppProcessingStatus.PROCESSED).build();
            appService.addAppData(app);
            log.info("Successfully established new APP trace tracking record for Passenger ID: {}",passengerId);
        } else {
            log.info("Failed to process Database sync : An active APP history footprint already exists for Passenger ID: {}", passengerId);
        }
    }

    public DCSRequestEvent prepareDcsRequestEvent(PNR pnr) {
        log.debug("Compiling Departure Control System (DCS) payload metrics for PNR tracking ID: {}", pnr.getPnrId());
        return DCSRequestEvent.builder().flightId(pnr.getFlight().getFlightId()).pnrId(pnr.getPnrId()).passengerId(String.valueOf(pnr.getPassenger().getId())).
                passengerName(pnr.getPassenger().getFullName()).build();
    }

}
