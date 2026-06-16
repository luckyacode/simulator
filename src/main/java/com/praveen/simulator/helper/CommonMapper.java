package com.praveen.simulator.helper;

import com.praveen.simulator.dto.*;
import com.praveen.simulator.entity.FlightManifest;
import com.praveen.simulator.entity.Passenger;
import com.praveen.simulator.kafka.events.CheckInEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommonMapper {

    @Mapping(ignore = true,target = "id")
    Passenger toPassenger(PassengerRequest passengerRequest);
    FlightManifest toFlightManifest(FlightRequest flightRequest);
    PnrEvent toPnrEvent(PnrRequest pnrRequest);
    CheckInEvent toCheckInEvent(CheckInRequest checkInRequest, String clearanceId);
}
