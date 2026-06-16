package com.praveen.simulator.kafka;

import com.praveen.simulator.dto.DCSRequest;
import com.praveen.simulator.dto.DocumentDetails;
import com.praveen.simulator.entity.*;
import com.praveen.simulator.helper.Utils;
import com.praveen.simulator.repository.AppRepository;
import com.praveen.simulator.service.CheckInResponseService;
import com.praveen.simulator.service.GovernmentClearanceService;
import com.praveen.simulator.service.PnrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final CheckInResponseService checkInResponseService;
    private final PnrService pnrService;
    private final AppRepository appRepository;
    private final KafkaPublisher kafkaPublisher;
    private final GovernmentClearanceService governmentClearanceService;
    private final KafkaService kafkaService;

    @KafkaListener(topics = "checkin-response", groupId = "group-id2")
    public void consumingCheckInRequest(@Payload String response, @Header(value = KafkaHeaders.RECEIVED_KEY) String clearanceId) {
        log.info("CheckInResponse Message received with key {} and message : {}", clearanceId, response);
        CheckInResponse checkInResponse = Utils.jsonToObject(response, CheckInResponse.class);
        log.info("Successfully Message Received : {}", checkInResponse);
        checkInResponseService.add(checkInResponse);
        governmentClearanceService.handleVettingResult(checkInResponse);
        PNR pnr = pnrService.getPnrById(checkInResponse.getPnrId());
        FlightManifest flight = pnr.getFlight();

        if (appRepository.findByGovernmentClearanceResponse_PassengerId(String.valueOf(pnr.getPassenger().getId())).isEmpty()) {
            APP app = APP.builder().appId(Utils.generateUniqueId()).pnrId(checkInResponse.getPnrId()).
                    flightId(flight.getFlightId()).departurePort(flight.getDepartureAirport()).
                    arrivalPort(flight.getArrivalAirport()).
                    passportNumber(Optional.ofNullable(pnr.getPassenger())
                            .map(Passenger::getDocumentDetails)
                            .map(DocumentDetails::getPassportNumber)
                            .orElse(null)).
                    createdDateTime(LocalDateTime.now()).governmentClearanceResponse(checkInResponse.getGovernmentClearanceResponse()).processingStatus(AppProcessingStatus.PROCESSED).build();
            log.info(" NEW APP Message is saved : {}", appRepository.save(app));
        } else {
            log.info(" Existing APP Message is present");
        }

        DCSRequest dcsRequest = DCSRequest.builder().flightId(flight.getFlightId()).pnrId(checkInResponse.getPnrId()).passengerId(String.valueOf(pnr.getPassenger().getId())).
        passengerName(pnr.getPassenger().getFullName()).build();
        kafkaService.processDcsMessage(dcsRequest);
    }


}
