package com.praveen.simulator.kafka.events;

import com.praveen.simulator.dto.BookingClass;
import com.praveen.simulator.dto.Channel;
import com.praveen.simulator.dto.TicketStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PnrEvent(String pnrId, LocalDateTime bookingDateTime, String bookingStatus, String flightId,
                       Integer passengerId, TicketStatus ticketStatus, Channel bookingChannel,
                       BookingClass bookingClass, String agencyId, String transactionId, Double totalAmount,
                       String currency) {
}
