package com.praveen.simulator.controller;

import com.praveen.simulator.dto.FlightRequest;
import com.praveen.simulator.entity.FlightManifest;
import com.praveen.simulator.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/flight")
@RequiredArgsConstructor
public class FlightController {
    private final FlightService flightService;

    @PostMapping("/scheduleFlight")
    public FlightManifest scheduleFlight(@RequestBody FlightRequest flightRequest){
        return flightService.scheduleFlight(flightRequest);
    }

    @PostMapping("/getAllFlights")
    public List<FlightManifest> getAllFlights(){
        return flightService.getAllFlights();
    }

    @PostMapping("/getActiveFlights")
    public List<FlightManifest> getActiveFlights(){
        return flightService.getActiveFlights();
    }
}
