package com.praveen.simulator.kafka;

import com.praveen.simulator.entity.CheckInResponse;
import com.praveen.simulator.entity.DlqTopic;
import com.praveen.simulator.helper.CommonMapper;
import com.praveen.simulator.helper.Utils;
import com.praveen.simulator.kafka.events.CheckInResponseEvent;
import com.praveen.simulator.kafka.events.KafkaGroups;
import com.praveen.simulator.kafka.events.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final KafkaService kafkaService;
    private final CommonMapper commonMapper;

    @RetryableTopic(attempts = "3" )
    @KafkaListener(topics = KafkaTopics.CheckIn.RESPONSES, groupId = KafkaGroups.DCS_SIMULATOR_GROUP)
    public void consumingCheckInRequest(@Payload CheckInResponseEvent response, @Header(value = KafkaHeaders.RECEIVED_KEY) String pnrId, Acknowledgment ack) {
        log.info("✓ Received CheckInResponse event via Kafka Broker partition. PNR Key: {}, Action: {}", pnrId, response);
//        CheckInResponseEvent checkInResponseEvent = commonMapper.toCheckInResponseEvent(response);
        kafkaService.processCheckInResponse(response);
        ack.acknowledge();
    }


    @DltHandler
    public void handleDlt(
            @Payload String failedEvent,
            @Header(KafkaHeaders.RECEIVED_KEY) String pnrId,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String deadTopic,
            @Header(name = "X-Exception-Message", required = false) String errorMessage) {


        log.error("🚨🛑 CRITICAL INTERCEPT: Appending message error signature to DB Triage Log table.");
        log.error("-> Partition Key: {} | Failed Topic: {}", pnrId, deadTopic);
        log.error("-> Failed PNR Locator: {}", pnrId);
        log.error("-> Source Dead Topic : {}", deadTopic);
        log.error("-> Failure Reason    : {}", errorMessage);
        log.error("-> Failure Message    : {}", failedEvent);

        DlqTopic dlqTopic = DlqTopic.builder().pnrId(pnrId!=null ? pnrId:"NO_PNR").
                sourceTopic(deadTopic).deadLetterTopic(deadTopic).
                reason(errorMessage).event(failedEvent).loggedAt(Instant.now()).resolved(Boolean.FALSE).build();
        kafkaService.saveDlq(dlqTopic);
    }
}
