package com.praveen.simulator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PNRRequest {
    private String PNRId;
    private String bookingDateTime;
    private String bookingStatus;
    private String flightId;
    private String passengerId;
    private TicketStatus ticketStatus;
    private Channel bookingChannel;
    private BookingClass bookingClass;
    private String agencyId;
    private String transactionId;
    private double totalAmount;
    private String currency;
}
