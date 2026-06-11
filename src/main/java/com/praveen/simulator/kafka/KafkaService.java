package com.praveen.simulator.kafka;

import com.praveen.simulator.dto.PNRRequest;
import com.praveen.simulator.helper.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaPublisher kafkaPublisher;

    public void sendPNRMessage(PNRRequest pnrRequest){
        log.info("Converting PNRRequest to json : {}",pnrRequest);
        String json = Utils.objectToJson(pnrRequest);
        log.info("Json value : {}",json);
        kafkaPublisher.publishPNRRequest(pnrRequest.getPNRId(),json);
    }

}
