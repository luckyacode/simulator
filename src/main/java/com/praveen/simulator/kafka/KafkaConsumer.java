package com.praveen.simulator.kafka;

import com.praveen.simulator.dto.DCSRequest;
import com.praveen.simulator.dto.DocumentDetails;
import com.praveen.simulator.entity.*;
import com.praveen.simulator.helper.Utils;
import com.praveen.simulator.other.CommonMapper;
import com.praveen.simulator.repository.APPRepository;
import com.praveen.simulator.service.CheckInResponseService;
import com.praveen.simulator.service.FlightService;
import com.praveen.simulator.service.PNRService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final PNRService pnrService;
    private final APPRepository appRepository;
    private final KafkaPublisher kafkaPublisher;

    @KafkaListener(topics = "checkin-response", groupId = "group-id2")
    public void consumingCheckInRequest(@Payload String response, @Header(value = KafkaHeaders.RECEIVED_KEY) String clearanceId) {
        log.info("CheckInResponse Message received with key {} and message : {}", clearanceId, response);
        CheckInResponse checkInResponse = Utils.jsonToObject(response, CheckInResponse.class);
        log.info("Successfully Message Received : {}", checkInResponse);
        checkInResponseService.add(checkInResponse);
        pnrService.handleVettingResult(checkInResponse);
        PNR pnr = pnrService.getPNRById(checkInResponse.getPnrId());
        FlightManifest flight = pnr.getFlight();
        APP app = APP.builder().appId(Utils.generateUniqueId()).pnrId(checkInResponse.getPnrId()).
                flightId(flight.getFlightId()).departurePort(flight.getDepartureAirport()).
                arrivalPort(flight.getArrivalAirport()).
                passportNumber(Optional.ofNullable(pnr.getPassenger())
                        .map(Passenger::getDocumentDetails)
                        .map(DocumentDetails::getPassportNumber)
                        .orElse(null)).
                createdDateTime(LocalDateTime.now()).governmentClearanceResponse(checkInResponse.getGovernmentClearanceResponse()).processingStatus(AppProcessingStatus.PROCESSED).build();
        log.info("APP Message is prepared ");
        log.info("APP Message is saved : {}",appRepository.save(app));
        DCSRequest dcsRequest = DCSRequest.builder().flightId(flight.getFlightId()).pnrId(checkInResponse.getPnrId()).passengerId(String.valueOf(pnr.getPassenger().getId())).build();
        String json = Utils.objectToJson(dcsRequest);
        log.info("Message is processing for DCS : {}",json);
        kafkaPublisher.sendDCSMessage(dcsRequest.getPnrId(),json);
    }


}
