package com.praveen.simulator.service;

import com.praveen.simulator.dto.PassengerRequest;
import com.praveen.simulator.entity.Passenger;
import com.praveen.simulator.helper.AirlineException;
import com.praveen.simulator.helper.CommonMapper;
import com.praveen.simulator.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final CommonMapper commonMapper;


    public Passenger addPassenger(PassengerRequest passengerRequest) {
        if(getPassengerById(passengerRequest.getId()).isPresent()){
            throw AirlineException.conflict("Cannot Create Booking, Passenger reference "+passengerRequest.getId()+" already exists.");
        }
        log.info("Adding passenger : {}",passengerRequest);
        Passenger savedPassenger = passengerRepository.save(commonMapper.toPassenger(passengerRequest));
        log.info("Passenger saved to db with id : {}",savedPassenger.getId());
        return savedPassenger;
    }

   public Passenger update(Passenger passenger) {
        log.info("Updating passenger : {}",passenger);
        Passenger savedPassenger = passengerRepository.save(passenger);
        log.info("Passenger updated to db  : {}",savedPassenger);
        return savedPassenger;
    }

    public Optional<Passenger> getPassengerById(int id) {
        return passengerRepository.findById(id);
    }

    public List<Passenger> getAllPassengers() {
        log.info("Fetching all passengers .....");
        return passengerRepository.findAll();
    }
}
