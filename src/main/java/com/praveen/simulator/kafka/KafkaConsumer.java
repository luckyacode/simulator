package com.praveen.simulator.kafka;

import com.praveen.simulator.helper.Utils;
import com.praveen.simulator.kafka.events.CheckInResponseEvent;
import com.praveen.simulator.kafka.events.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final KafkaService kafkaService;

    @KafkaListener(topics = KafkaTopics.CheckIn.RESPONSES, groupId = "group-id2")
    public void consumingCheckInRequest(@Payload String response, @Header(value = KafkaHeaders.RECEIVED_KEY) String pnrId) {
        log.info("✓ Received CheckInResponse event via Kafka Broker partition. PNR Key: {}, Action: {}", pnrId, response);
        CheckInResponseEvent checkInResponseEvent = Utils.jsonToObject(response, CheckInResponseEvent.class);
        kafkaService.processCheckInResponse(checkInResponseEvent);
    }

}
