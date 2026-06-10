package com.praveen.simulator.dto;

public record DcsCheckInRequest(
        AppRequest app,
        String seatNumber,
        double baggageWeight
) {}