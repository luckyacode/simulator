package com.praveen.simulator;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final CommonMapper commonMapper;

    public Passenger addPassenger(Passenger passenger) {
        Passenger savedPassenger = passengerRepository.save(passenger);
        System.out.println("Passenger pushed to db "+savedPassenger.getId());
        return savedPassenger;
    }

    public Optional<Passenger> getPassengerById(int id) {
        return passengerRepository.findById(id);
    }

    public List<Passenger> fetchAllPassenger() {
        return passengerRepository.findAll();
    }

    public String generateRandomPassenger(int size) {
       List<PassengerRequest> passengerRequests = Utils.fetchPassengerFromFile(size);
       passengerRequests.stream().map(commonMapper::toPassenger).forEach(this::addPassenger);
       return "Success";
    }
}
