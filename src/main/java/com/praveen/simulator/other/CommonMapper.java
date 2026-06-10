package com.praveen.simulator.other;

import com.praveen.simulator.dto.FlightRequest;
import com.praveen.simulator.entity.FlightManifest;
import com.praveen.simulator.entity.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommonMapper {

    @Mapping(ignore = true,target = "id")
    Passenger toPassenger(PassengerRequest passengerRequest);
    FlightManifest toFlightManifest(FlightRequest flightRequest);

}
