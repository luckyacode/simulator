package com.praveen.simulator;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommonMapper {

    Passenger toPassenger(PassengerRequest passengerRequest);

}
