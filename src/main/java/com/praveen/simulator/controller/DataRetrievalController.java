package com.praveen.simulator.controller;

import com.praveen.simulator.dto.PassengerRequest;
import com.praveen.simulator.helper.Utils;
import com.praveen.simulator.dto.FlightDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fetchData")
@RequiredArgsConstructor
public class DataRetrievalController {
    private final FlightDetailRepository flightDetailRepository;
    private final PassengerRequestRepository passengerRequestRepository;

    @GetMapping("/flights/{size}")
    public List<FlightDetail> fetchFlights(@PathVariable int size){
        List<FlightDetail> list = Utils.fetchFlightFromFile(size);
        flightDetailRepository.saveAll(list);
           return  list;
    }

    @GetMapping("/passenger/{size}")
    public List<PassengerRequest> fetchPassenger(@PathVariable int size){
        List<PassengerRequest> list =  Utils.fetchPassengerFromFile(size);
        passengerRequestRepository.saveAll(list);
        return list;
    }

    @GetMapping("/flightsT")
    public List<FlightDetail> fetchFlightsT(){
           return  flightDetailRepository.findAll();
    }

    @GetMapping("/passengerT")
    public List<PassengerRequest> fetchPassengerT(){
        return  passengerRequestRepository.findAll();
    }


}
