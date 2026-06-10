package com.praveen.simulator.entity;


import com.praveen.simulator.dto.Channel;
import com.praveen.simulator.dto.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class PNR {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String pnrId;
    @OneToOne
    private Passenger passengers;
    @ManyToOne
    private FlightManifest flight;
    @Enumerated(value = EnumType.STRING)
    private TicketStatus ticketStatus;
    @Enumerated(value = EnumType.STRING)
    private Channel bookingChannel;
    private String transactionId;
    private double totalAmountPaid;
    private String currency;
}