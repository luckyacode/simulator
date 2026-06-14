package com.praveen.simulator.controller;

import com.praveen.simulator.dto.BookingRequest;
import com.praveen.simulator.dto.CheckInRequest;
import com.praveen.simulator.dto.PNRRequest;
import com.praveen.simulator.entity.CheckInResponse;
import com.praveen.simulator.service.BookingService;
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
        return bookingService.checkInPassenger(checkInRequest);
    }

    @GetMapping("/checkInStatusByClearanceId/{clearanceId}")
    public CheckInResponse checkInStatusByClearanceId(@PathVariable String clearanceId){
        return bookingService.checkInStatusByClearanceId(clearanceId);
    }

    @GetMapping("/checkInStatusByPassengerId/{passengerId}")
    public CheckInResponse checkInStatusByPassengerId(@PathVariable String passengerId){
        return bookingService.checkInStatusByPassengerId(passengerId);
    }
}
