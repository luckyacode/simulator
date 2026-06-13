package com.praveen.simulator.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightDetail {
    // Structural vectors from your CSV dataset
    private String flightId;
    private String airline;
    private String status;
    private String departureAirport;
    private String departureCountry;
    private String arrivalAirport;
    private String arrivalCountry;
    private String equipment;

    // Separated Date and Time Tracking
    private LocalDate departureDate;
    private LocalTime departureTime;
    private LocalDate arrivalDate;
    private LocalTime arrivalTime;

    // 🌟 BRAND NEW: Consolidated Production DateTime Fields
    private LocalDateTime scheduledDepartureDateTime;
    private LocalDateTime scheduledArrivalDateTime;

    public FlightDetail(String[] csvRow) {
        this.airline = csvRow[0];
        this.departureAirport = csvRow[1];
        this.departureCountry = csvRow[4];
        this.arrivalAirport = csvRow[5];
        this.arrivalCountry = csvRow[8];
        this.equipment = "Airline";
        this.flightId = csvRow[10];
        // Execute the programmatic dynamic scheduling sequence
        generateFullSchedule();
    }

    private void generateFullSchedule() {
        // 1. Unique Flight identifier
        int randomNum = (int) (Math.random() * 900) + 100;
        this.flightId = this.airline + "-" + randomNum;

        // 2. Schedule Departure (For example, assume flights are generated for "Today")
        int hour = (int) (Math.random() * 24);
        int[] minutes = {0, 15, 30, 45};
        int minute = minutes[(int) (Math.random() * minutes.length)];

        // Combine into a full timestamp anchor and save to your new field!
        this.scheduledDepartureDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, minute));

        // Split and assign to separate date and time properties for backwards compatibility
        this.departureDate = this.scheduledDepartureDateTime.toLocalDate();
        this.departureTime = this.scheduledDepartureDateTime.toLocalTime();

        // 3. Dynamically append random flight duration (between 2 and 14 hours)
        int flightDurationHours = (int) (Math.random() * 12) + 2;

        // Java automatically handles rolling over midnight and increments the day.
        // Save it directly to your second new field!
        this.scheduledArrivalDateTime = this.scheduledDepartureDateTime.plusHours(flightDurationHours);

        // Split and assign to separate arrival date and time properties
        this.arrivalDate = this.scheduledArrivalDateTime.toLocalDate();
        this.arrivalTime = this.scheduledArrivalDateTime.toLocalTime();

        // 4. Evaluate status attributes
        String[] statuses = {"ON TIME", "DELAYED", "BOARDING"};
        this.status = statuses[(int) (Math.random() * statuses.length)];
    }

    @Override
    public String toString() {
        // Format layout patterns using the consolidated LocalDateTime properties directly
        DateTimeFormatter fullDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return String.format("[%s] %s | %s (%s) DEP: %s -> %s (%s) ARR: %s | Type: %s",
                status, flightId,
                departureAirport, departureCountry, scheduledDepartureDateTime.format(fullDateTimeFormat),
                arrivalAirport, arrivalCountry, scheduledArrivalDateTime.format(fullDateTimeFormat),
                equipment);
    }
}