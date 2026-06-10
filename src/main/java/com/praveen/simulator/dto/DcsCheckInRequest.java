package com.praveen.simulator.dto;

import com.praveen.simulator.model.AppRecord;
import com.praveen.simulator.model.FlightDetail;

public record DcsCheckInRequest(
        AppRequest app,
        String seatNumber,
        double baggageWeight
) {}