package com.praveen.simulator.kafka.events;

import com.praveen.simulator.dto.DocumentDetails;
import lombok.Builder;

@Builder
public record CheckInEvent(String pnrId, String clearanceId, DocumentDetails documentDetails) {
}
