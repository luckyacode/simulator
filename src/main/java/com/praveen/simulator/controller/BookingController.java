package com.praveen.simulator.controller;

import com.praveen.simulator.dto.ApiResponse;
import com.praveen.simulator.dto.BookingRequest;
import com.praveen.simulator.dto.CheckInRequest;
import com.praveen.simulator.dto.PnrRequest;
import com.praveen.simulator.entity.CheckInResponse;
import com.praveen.simulator.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    /**
     * Create a new passenger booking.
     * Fixed: Returns HTTP 201 Created instead of 200 OK.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PnrRequest>> createPassengerBooking(@RequestBody BookingRequest bookingRequest) {
        PnrRequest result = bookingService.createPassengerBooking(
                bookingRequest.passenger(),
                bookingRequest.flightId(),
                bookingRequest.amount()
        );
        return ApiResponse.created(result, "Passenger Booking Initiated");
    }

    /**
     * Process a check-in.
     * Fixed: URL path changed from verb (/checkIn) to plural noun (/check-ins).
     * Fixed: If this creates a check-in record, consider changing to ApiResponse.created().
     */
    @PostMapping("/check-ins")
    public ResponseEntity<ApiResponse<String>> checkInPassenger(@RequestBody CheckInRequest checkInRequest) {
        String result = bookingService.checkInPassenger(checkInRequest);
        return ApiResponse.ok(result, "CheckIn Initiated Successfully");
    }

    /**
     * Get Check-In status filtered by search criteria.
     * URL Example 1: GET /api/v1/bookings/check-ins?clearanceId=CLR123
     * URL Example 2: GET /api/v1/bookings/check-ins?passengerId=PAX456
     */
    @GetMapping("/check-ins")
    public ResponseEntity<ApiResponse<CheckInResponse>> getCheckInStatus(
            @RequestParam(value = "clearanceId", required = false) String clearanceId,
            @RequestParam(value = "passengerId", required = false) String passengerId) {

        CheckInResponse response;

        if (clearanceId != null) {
            response = bookingService.checkInStatusByClearanceId(clearanceId);
        } else if (passengerId != null) {
            response = bookingService.checkInStatusByPassengerId(passengerId);
        } else {
            // Production tip: Always handle the edge case where no query params are provided
            return ApiResponse.badRequest("Either clearanceId or passengerId query parameter must be provided.");
        }

        return ApiResponse.ok(response, "CheckIn Status Retrieved Successfully");
    }
}