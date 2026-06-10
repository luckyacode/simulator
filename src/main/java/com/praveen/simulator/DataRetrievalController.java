package com.praveen.simulator;

import com.praveen.simulator.model.FlightDetail;
import com.praveen.simulator.other.PassengerRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fetchData")
public class DataRetrievalController {

    @GetMapping("/flights/{size}")
    public List<FlightDetail> fetchFlights(@PathVariable int size){
       return  Utils.fetchFlightFromFile(size);
    }

    @GetMapping("/passenger/{size}")
    public List<PassengerRequest> fetchPassenger(@PathVariable int size){
        return Utils.fetchPassengerFromFile(size);
    }
}
