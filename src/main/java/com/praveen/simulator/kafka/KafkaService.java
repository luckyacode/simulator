package com.praveen.simulator.kafka;

import com.praveen.simulator.dto.DCSRequest;
import com.praveen.simulator.dto.PnrEvent;
import com.praveen.simulator.kafka.events.CheckInEvent;
import com.praveen.simulator.dto.PnrRequest;
import com.praveen.simulator.helper.Utils;
import com.praveen.simulator.kafka.events.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaPublisher kafkaPublisher;

    public void processPnrEvent(PnrEvent pnrEvent) {
        if (pnrEvent == null || pnrEvent.pnrId() == null) {
            log.error("Aborting Kafka publish: PnrEvent or PNR ID tracking payload is null.");
            return;
        }
        log.info("Dispatching PnrEvent message to topic: {} for PNR: {}", KafkaTopics.PNR_EVENTS, pnrEvent.pnrId());
        String json = Utils.objectToJson(pnrEvent);
        log.info("Json value : {}",json);
        kafkaPublisher.sendKafkaEvent(KafkaTopics.PNR_EVENTS, pnrEvent.pnrId(),pnrEvent);

//        kafkaPublisher.publishKafkaMessage(KafkaTopics.PNR_EVENTS, pnrEvent.pnrId(), pnrEvent);
    }

    public void processCheckInEvent(CheckInEvent checkInEvent) {
        if (checkInEvent == null || checkInEvent.pnrId() == null) {
            log.error("Aborting Kafka publish: CheckInEvent or associated PNR identifier is missing.");
            return;
        }


        log.info("Dispatching CheckInEvent to topic: {} matching PNR: {} and Tracking Ref: {}",
                KafkaTopics.CheckIn.REQUESTS, checkInEvent.pnrId(), checkInEvent.clearanceId());
        String json = Utils.objectToJson(checkInEvent);
        kafkaPublisher.sendKafkaEvent(KafkaTopics.CheckIn.REQUESTS, checkInEvent.clearanceId(),checkInEvent);

//        kafkaPublisher.publishKafkaMessage(KafkaTopics.CheckIn.REQUESTS, checkInEvent.pnrId(), checkInEvent);
    }

    public void processDcsMessage(DCSRequest dcsRequest) {
        if (dcsRequest == null || dcsRequest.getPnrId() == null) {
            log.error("Aborting Kafka publish: DCSRequest metadata or payload body is null.");
            return;
        }

        log.info("Dispatching DCS Event message to topic: {} for PNR ID: {}", KafkaTopics.DCS_EVENTS, dcsRequest.getPnrId());

        String json = Utils.objectToJson(dcsRequest);
        kafkaPublisher.sendKafkaEvent(KafkaTopics.DCS_EVENTS, dcsRequest.getPnrId(), dcsRequest);

//        kafkaPublisher.publishKafkaMessage(KafkaTopics.DCS_EVENTS, dcsRequest.getPnrId(), dcsRequest);
    }
}
