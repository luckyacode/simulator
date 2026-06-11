package com.praveen.simulator.controller;

import com.praveen.simulator.dto.*;
import com.praveen.simulator.entity.CheckInResponse;
import com.praveen.simulator.service.BookingService;
import com.praveen.simulator.model.AppRecord;
import com.praveen.simulator.model.DcsRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/createPassengerBooking")
    public PNRRequest creatingPassengerBooking(@RequestBody BookingRequest bookingRequest){
        return bookingService.createPassengerBooking(bookingRequest.passenger(),bookingRequest.flightId(),bookingRequest.amount());
    }

    @PostMapping("/checkIn")
    public String checkInService(@RequestBody CheckInRequest checkInRequest){
        bookingService.checkInPassenger(checkInRequest);
        return "Success CheckIn Process";
    }

    @GetMapping("/checkInStatusByClearanceId/{clearanceId}")
    public CheckInResponse checkInStatusByClearanceId(@PathVariable String clearanceId){
        return bookingService.checkInStatusByClearanceId(clearanceId);
    }

    @GetMapping("/checkInStatusByPassengerId/{passengerId}")
    public CheckInResponse checkInStatusByPassengerId(@PathVariable String passengerId){
        return bookingService.checkInStatusByPassengerId(passengerId);
    }

//    @PostMapping("/processAPP")
//    public AppRecord processAPP(@RequestBody AppRequest appRequest){
//        return bookingService.processAPPData(appRequest.pnr(),appRequest.passportNumber(),appRequest.issuingCountry(),appRequest.gender());
//    }

    @PostMapping("/processDCS")
    public DcsRecord processDCS(@RequestBody DcsCheckInRequest dcsCheckInRequest) {
        return bookingService.processDCSData(dcsCheckInRequest.app(),dcsCheckInRequest.seatNumber(),dcsCheckInRequest.baggageWeight());
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
