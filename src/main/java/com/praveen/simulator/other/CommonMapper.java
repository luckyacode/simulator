package com.praveen.simulator.other;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommonMapper {

    @Mapping(ignore = true,target = "id")
    Passenger toPassenger(PassengerRequest passengerRequest);

}
