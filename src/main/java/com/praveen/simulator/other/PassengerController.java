package com.praveen.simulator.other;

import com.praveen.simulator.entity.Passenger;
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
    public String addPassenger(@RequestBody Passenger passenger){
        passengerService.addPassenger(passenger);
        return "Success ";
    }

    @GetMapping("/getById/{id}")
    public Passenger getPassenger(int id) throws Exception {
        return passengerService.getPassengerById(id).orElseThrow(()-> new Exception("Passenger not found"));
    }

    @GetMapping("/fetchFromDb")
    public List<Passenger> fetchPassenger(){
        return passengerService.fetchAllPassenger();
    }

    @GetMapping("/generateRandomPassenger/{size}")
    public String generateRandomPassenger(int size){
        return passengerService.generateRandomPassenger(size);
    }
}
