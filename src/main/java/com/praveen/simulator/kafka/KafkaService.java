package com.praveen.simulator.kafka;

import com.praveen.simulator.dto.PnrEvent;
import com.praveen.simulator.kafka.events.CheckInEvent;
import com.praveen.simulator.dto.PnrRequest;
import com.praveen.simulator.helper.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaPublisher kafkaPublisher;

    public void sendPNRMessage(PnrRequest pnrRequest){
        log.info("Converting PnrRequest to json : {}",pnrRequest);
        String json = Utils.objectToJson(pnrRequest);
        log.info("Json value : {}",json);
        kafkaPublisher.publishPnrRequest(pnrRequest.getPNRId(),json);
    }

    public void sendPNRMessage(PnrEvent pnrEvent){
        log.info("Converting PnrEvent to json : {}",pnrEvent);
        String json = Utils.objectToJson(pnrEvent);
        log.info("Json value : {}",json);
        kafkaPublisher.publishPnrRequest(pnrEvent.PnrId(),json);
    }

    public void checkInOnAirport(CheckInEvent checkInEvent) {
        log.info("Checking process called...{}",checkInEvent);
        String json = Utils.objectToJson(checkInEvent);
        kafkaPublisher.sendCheckInRequestMessage(checkInEvent.clearanceId(),json);
    }
}
