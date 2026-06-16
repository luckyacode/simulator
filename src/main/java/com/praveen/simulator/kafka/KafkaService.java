package com.praveen.simulator.kafka;

import com.praveen.simulator.kafka.events.PnrEvent;
import com.praveen.simulator.entity.*;
import com.praveen.simulator.helper.CommonMapper;
import com.praveen.simulator.helper.Utils;
import com.praveen.simulator.kafka.events.*;
import com.praveen.simulator.service.CheckInResponseService;
import com.praveen.simulator.service.GovernmentClearanceService;
import com.praveen.simulator.service.PnrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaPublisher kafkaPublisher;
    private final CommonMapper commonMapper;
    private final CheckInResponseService checkInResponseService;
    private final PnrService pnrService;
    private final GovernmentClearanceService governmentClearanceService;
    private final KafkaHelper kafkaHelper;

    public void processPnrEvent(PnrEvent pnrEvent) {
        if (pnrEvent == null || pnrEvent.pnrId() == null) {
            log.error("Aborting Kafka publish: PnrEvent or PNR ID tracking payload is null.");
            return;
        }
        log.info("Dispatching PnrEvent message to topic: {} for PNR: {}", KafkaTopics.PNR_EVENTS, pnrEvent.pnrId());
        String json = Utils.objectToJson(pnrEvent);
        log.info("Json value : {}", json);
        kafkaPublisher.sendKafkaEvent(KafkaTopics.PNR_EVENTS, pnrEvent.pnrId(), json);
    }

    public void processCheckInEvent(CheckInEvent checkInEvent) {
        if (checkInEvent == null || checkInEvent.pnrId() == null) {
            log.error("Aborting Kafka publish: CheckInEvent or associated PNR identifier is missing.");
            return;
        }
        log.info("Dispatching CheckInEvent to topic: {} matching PNR: {} and Tracking Ref: {}", KafkaTopics.CheckIn.REQUESTS, checkInEvent.pnrId(), checkInEvent.clearanceId());
        String json = Utils.objectToJson(checkInEvent);
        kafkaPublisher.sendKafkaEvent(KafkaTopics.CheckIn.REQUESTS, checkInEvent.pnrId(), json);
    }

    public void processDcsMessage(DCSRequestEvent dcsRequestEvent) {
        if (dcsRequestEvent == null || dcsRequestEvent.getPnrId() == null) {
            log.error("Aborting Kafka publish: DCSRequestEvent metadata or payload body is null.");
            return;
        }
        log.info("Dispatching DCSRequestEvent message to topic: {} for PNR ID: {}", KafkaTopics.DCS_EVENTS, dcsRequestEvent.getPnrId());
        String json = Utils.objectToJson(dcsRequestEvent);
        kafkaPublisher.sendKafkaEvent(KafkaTopics.DCS_EVENTS, dcsRequestEvent.getPnrId(), json);
    }

    @Transactional
    public void processCheckInResponse(CheckInResponseEvent checkInResponseEvent) {
        PNR pnr = pnrService.getPnrById(checkInResponseEvent.getPnrId());
        CheckInResponse checkInResponse = commonMapper.toCheckInResponse(checkInResponseEvent);
        checkInResponseService.add(checkInResponse);
        log.info("CheckInResponse processing for save in db record : {}",checkInResponse.getPnrId());
        governmentClearanceService.handleVettingResult(checkInResponse);
        log.info("Processing for commiting app data for record : {}",checkInResponse.getPnrId());
        kafkaHelper.processAppMessage(pnr,checkInResponse);
        log.info("Preparing for DCS request : {}",checkInResponse.getPnrId());
        DCSRequestEvent dcsRequestEvent = kafkaHelper.prepareDcsRequestEvent(pnr);
        log.info("Processing DCS request event : {}",dcsRequestEvent);
        processDcsMessage(dcsRequestEvent);
    }


}
