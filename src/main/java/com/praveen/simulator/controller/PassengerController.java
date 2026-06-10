package com.praveen.simulator.controller;

import com.praveen.simulator.dto.PassengerRequest;
import com.praveen.simulator.entity.Passenger;
import com.praveen.simulator.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passenger")
@RequiredArgsConstructor
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    @PostMapping("/add")
    public Passenger addPassenger(@RequestBody PassengerRequest passengerRequest) {
        return passengerService.addPassenger(passengerRequest);
    }

    @GetMapping("/getById/{id}")
    public Passenger getPassenger(int id) throws Exception {
        return passengerService.getPassengerById(id).orElseThrow(() -> new Exception("Passenger not found"));
    }

    @GetMapping("/getAllPassengers")
    public List<Passenger> getAllPassengers() {
        return passengerService.getAllPassengers();
    }
}
