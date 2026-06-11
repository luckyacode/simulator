package com.praveen.simulator.kafka;

import com.praveen.simulator.dto.CheckInRequest;
import com.praveen.simulator.dto.PNRRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaPublisher {
    private final KafkaTemplate<String,String> kafkaTemplate;

    public void publishPNRRequest(String id,String pnrRequest){
        CompletableFuture<SendResult<String, String>> result = kafkaTemplate.send("pnr-topic",id,pnrRequest);
        result.whenComplete((((object, exception) -> {
            if(exception==null)
                log.info("Successfully published kafka message to  : pnr-topic");
            else {
                log.error("failed to  published kafka message to  : pnr-topic ",exception);
            }
        })));
    }

    public void sendCheckInRequestMessage(String clearanceId, String json) {
        CompletableFuture<SendResult<String, String>> result = kafkaTemplate.send("checkin-topic", clearanceId, json);
        result.whenComplete((((object, exception) -> {
            if (exception == null)
                log.info("Successfully published kafka message to  : checkin-topic");
            else {
                log.error("failed to  published kafka message to  : checkin-topic ", exception);
            }
        })));
    }

    public void sendDCSMessage(String key, String json) {
        log.info("Publish to DCS request : {}",json);
        CompletableFuture<SendResult<String, String>> result = kafkaTemplate.send("dcs-topic",key,json);
        result.whenComplete((((object, exception) -> {
            if(exception==null)
                log.info("Successfully published kafka message to  : dcs-topic");
            else {
                log.error("failed to  published kafka message to  : dcs-topic ",exception);
            }
        })));

    }
}
