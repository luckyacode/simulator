package com.praveen.simulator.helper;

import com.praveen.simulator.dto.*;
import com.praveen.simulator.entity.CheckInResponse;
import com.praveen.simulator.entity.FlightManifest;
import com.praveen.simulator.entity.Passenger;
import com.praveen.simulator.kafka.events.CheckInEvent;
import com.praveen.simulator.kafka.events.CheckInResponseEvent;
import com.praveen.simulator.kafka.events.DCSRequestEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommonMapper {

    @Mapping(ignore = true,target = "id")
    Passenger toPassenger(PassengerRequest passengerRequest);
    FlightManifest toFlightManifest(FlightRequest flightRequest);
    PnrEvent toPnrEvent(PnrRequest pnrRequest);
    CheckInEvent toCheckInEvent(CheckInRequest checkInRequest, String clearanceId);

    CheckInResponse toCheckInResponse(CheckInResponseEvent checkInResponseEvent);

    DCSRequestEvent toDcsRequestEvent(DCSRequest dcsRequest);
}
