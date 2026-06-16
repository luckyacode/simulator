package com.praveen.simulator.kafka;

import com.praveen.simulator.helper.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publishKafkaMessage(String kafkaTopic, String id, String request) {
        log.debug("Attempting to publish message with key: {} to topic: {}", id, kafkaTopic);

        CompletableFuture<SendResult<String, String>> result = kafkaTemplate.send(kafkaTopic, id, request);

        result.whenComplete((sendResult, exception) -> {
            if (exception == null) {
                var metadata = sendResult.getRecordMetadata();
                log.info("Successfully published kafka message. Topic: {} | Key: {} | Partition: {} | Offset: {}", metadata.topic(), id, metadata.partition(), metadata.offset());
            } else {
                log.error("Failed to publish kafka message. Topic: {} | Key: {} | Reason: {}", kafkaTopic, id, exception.getMessage(), exception);
            }
        });
    }

    public void sendKafkaEvent(String topic, String pnrId, Object eventPayload) {
        if (pnrId == null || pnrId.isBlank()) {
            log.error("💥 SYSTEM BLOCK: Attempted to publish event to topic [{}] without a valid PNR key! Payload aborted to prevent out-of-order state corruption.", topic);
            throw new IllegalArgumentException("Kafka partition routing key (pnrId) cannot be null or empty for stateful changes.");
        }

        log.debug("Routing stateful event to topic [{}] pinned to partition key [PNR: {}]", topic, pnrId);
        String json = Utils.objectToJson(eventPayload);
        kafkaTemplate.send(topic, pnrId, json)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("🚨 KAFKA BROKER FAILURE: PNR [{}] transaction failed to append. Error: {}", pnrId, ex.getMessage());
                    } else {
                        log.info("✓ Event committed on topic {} to partition {} at offset {} for PNR: {}",
                                topic,
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset(),
                                pnrId);
                    }
                });
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
