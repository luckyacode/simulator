package com.praveen.simulator.entity;

import com.praveen.simulator.dto.FlightStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class FlightManifest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String airline;
    private String sourceAirport;
    private String departureCountry;
    private String destAirport;
    private String arrivalCountry;
    private String equipment;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private LocalDate arrivalDate;
    private LocalTime arrivalTime;
    private String flightId;
    private String status;
    private FlightStatus flightStatus;
}