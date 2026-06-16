package com.praveen.simulator.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PnrEvent(String pnrId, LocalDateTime bookingDateTime, String bookingStatus, String flightId,
                       Integer passengerId, TicketStatus ticketStatus, Channel bookingChannel,
                       BookingClass bookingClass, String agencyId, String transactionId, Double totalAmount,
                       String currency) {
}
