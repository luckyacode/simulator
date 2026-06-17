package com.praveen.simulator.kafka.events;

import lombok.*;

@Getter
@Setter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DCSRequestEvent {
    private String flightId;
    private String pnrId;
    private String passengerId;
    private String passengerName;
}
