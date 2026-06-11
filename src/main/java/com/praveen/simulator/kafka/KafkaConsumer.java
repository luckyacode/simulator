package com.praveen.simulator.kafka;

import com.praveen.simulator.entity.CheckInResponse;
import com.praveen.simulator.helper.Utils;
import com.praveen.simulator.service.CheckInResponseService;
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
    private final CheckInResponseService checkInResponseService;

    @KafkaListener(topics = "checkin-response", groupId = "group-id2")
    public void consumingCheckInRequest(@Payload String response, @Header(value = KafkaHeaders.RECEIVED_KEY) String clearanceId) {
        log.info("CheckInResponse Message received with key {} and message : {}", clearanceId, response);
        CheckInResponse checkInResponse = Utils.jsonToObject(response, CheckInResponse.class);
        log.info("Successfully Message Received : {}", checkInResponse);
        checkInResponseService.add(checkInResponse);
    }


}
