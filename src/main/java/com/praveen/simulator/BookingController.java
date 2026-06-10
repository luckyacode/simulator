package com.praveen.simulator;

import com.praveen.simulator.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/createPassengerBooking")
    public PnrRecord creatingPassengerBooking(@RequestBody PassengerRecord passengerRecord, @RequestBody FlightInfo flightInfo,@RequestParam double amount){
        return bookingService.createPassengerBooking(passengerRecord,flightInfo,amount);
    }

    @PostMapping("/processAPP")
    public AppRecord processAPP(@RequestParam PnrRecord pnr,@RequestParam String passportNum,@RequestParam String countryCode,@RequestParam String gender){
        return bookingService.processAPPData(pnr,passportNum,countryCode,gender);
    }

    @PostMapping("/processDCS")
    public DcsRecord processDCS(PnrRecord pnr, AppRecord app, FlightInfo flight, String targetSeat, double bagWeight) {
        return bookingService.processDCSData(pnr,app,flight,targetSeat,bagWeight);
    }


//    @GetMapping("/getById/{id}")
//    public Passenger getPassenger(int id) throws Exception {
//        return passengerService.getPassengerById(id).orElseThrow(()-> new Exception("Passenger not found"));
//    }
//
//    @GetMapping("/fetchFromDb")
//    public List<Passenger> fetchPassenger(){
//        return passengerService.fetchAllPassenger();
//    }
//
//    @GetMapping("/generateRandomPassenger/{size}")
//    public String generateRandomPassenger(int size){
//        return passengerService.generateRandomPassenger(size);
//    }

}
