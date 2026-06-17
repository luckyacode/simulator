package com.praveen.simulator.kafka;

import com.praveen.simulator.entity.CheckInResponse;
import com.praveen.simulator.entity.DlqTopic;
import com.praveen.simulator.entity.PNR;
import com.praveen.simulator.helper.CommonMapper;
import com.praveen.simulator.kafka.events.*;
import com.praveen.simulator.repository.DlqTopicRepository;
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
    private final DlqTopicRepository dlqTopicRepository;

    public void processPnrEvent(PnrEvent pnrEvent) {
        if (pnrEvent == null || pnrEvent.pnrId() == null) {
            log.error("Aborting Kafka publish: PnrEvent or PNR ID tracking payload is null.");
            return;
        }
        log.info("Dispatching PnrEvent message to topic: {} for PNR: {}", KafkaTopics.PNR_EVENTS, pnrEvent.pnrId());

        kafkaPublisher.sendKafkaEvent(KafkaTopics.PNR_EVENTS, pnrEvent.pnrId(), commonMapper.toAvroPnrEvent(pnrEvent));
    }

    public void processCheckInEvent(CheckInEvent checkInEvent) {
        if (checkInEvent == null || checkInEvent.pnrId() == null) {
            log.error("Aborting Kafka publish: CheckInEvent or associated PNR identifier is missing.");
            return;
        }
        log.info("Dispatching CheckInEvent to topic: {} matching PNR: {} and Tracking Ref: {}", KafkaTopics.CheckIn.REQUESTS, checkInEvent.pnrId(), checkInEvent.clearanceId());
        kafkaPublisher.sendKafkaEvent(KafkaTopics.CheckIn.REQUESTS, checkInEvent.pnrId(), commonMapper.toAvroCheckInRequest(checkInEvent));
    }

    public void processDcsMessage(DCSRequestEvent dcsRequestEvent) {
        if (dcsRequestEvent == null || dcsRequestEvent.getPnrId() == null) {
            log.error("Aborting Kafka publish: DCSRequestEvent metadata or payload body is null.");
            return;
        }
        log.info("Dispatching DCSRequestEvent message to topic: {} for PNR ID: {}", KafkaTopics.DCS_EVENTS, dcsRequestEvent.getPnrId());
        kafkaPublisher.sendKafkaEvent(KafkaTopics.DCS_EVENTS, dcsRequestEvent.getPnrId(), commonMapper.toDcsRequestEvent(dcsRequestEvent));
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

    public void saveDlq(DlqTopic dlqTopic) {
        dlqTopicRepository.save(dlqTopic);
        log.info("DLQ Message Record commited in database");
    }


}
