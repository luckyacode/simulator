package com.praveen.simulator.kafka;

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
}
