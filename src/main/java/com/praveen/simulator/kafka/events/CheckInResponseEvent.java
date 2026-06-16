package com.praveen.simulator.kafka.events;

import com.praveen.simulator.entity.GovernmentClearanceResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CheckInResponseEvent{
    String pnrId;
    GovernmentClearanceResponse governmentClearanceResponse;
}
