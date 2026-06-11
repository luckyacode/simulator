package com.praveen.simulator.dto;

public record BookingRequest(PassengerRequest passenger, String flightId, double amount) {
}
