package com.praveen.simulator.dto;

import com.praveen.simulator.model.PassengerRecord;
import com.praveen.simulator.model.FlightDetail;

public record BookingRequest(
        PassengerRecord passenger,
        FlightDetail flight,
        double amount
) {
}
