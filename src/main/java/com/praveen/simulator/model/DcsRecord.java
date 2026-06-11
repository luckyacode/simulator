package com.praveen.simulator.model;

import com.praveen.simulator.entity.FlightManifest;

import java.time.LocalDateTime;
import java.util.List;

public record DcsRecord(
        String ticketNumber,
        String pnrLocator,
        FlightManifest flight,
        String seatNumber,                 // Assigned seat (e.g., "12B")
        String boardingSequenceNumber,     // sequence order (e.g., "045")
        String passengerStatus,            // e.g., "CHECKED_IN", "BOARDED", "NO_SHOW"
        List<BaggageInfo> checkedBaggage,  // Tracked bags
        LocalDateTime checkInTime,
        boolean isFrequentFlyerUpgrade
) {}
