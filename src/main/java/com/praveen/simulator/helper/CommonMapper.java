package com.praveen.simulator.helper;

import com.praveen.simulator.dto.CheckInRequest;
import com.praveen.simulator.dto.FlightRequest;
import com.praveen.simulator.dto.PassengerRequest;
import com.praveen.simulator.dto.PnrRequest;
import com.praveen.simulator.entity.CheckInResponse;
import com.praveen.simulator.entity.FlightManifest;
import com.praveen.simulator.entity.Passenger;
import com.praveen.simulator.kafka.events.CheckInEvent;
import com.praveen.simulator.kafka.events.CheckInResponseEvent;
import com.praveen.simulator.kafka.events.DCSRequestEvent;
import com.praveen.simulator.kafka.events.PnrEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface CommonMapper {

    @Mapping(ignore = true,target = "id")
    Passenger toPassenger(PassengerRequest passengerRequest);
    FlightManifest toFlightManifest(FlightRequest flightRequest);
    PnrEvent toPnrEvent(PnrRequest pnrRequest);
    CheckInEvent toCheckInEvent(CheckInRequest checkInRequest, String clearanceId);

    CheckInResponse toCheckInResponse(CheckInResponseEvent checkInResponseEvent);

    default Instant map(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

}
