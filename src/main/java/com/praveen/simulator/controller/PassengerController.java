package com.praveen.simulator.controller;

import com.praveen.simulator.dto.ApiResponse;
import com.praveen.simulator.dto.PassengerRequest;
import com.praveen.simulator.entity.Passenger;
import com.praveen.simulator.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor // Automatically creates constructor for final fields
public class PassengerController {

    // Fixed: Made final so Lombok's @RequiredArgsConstructor can inject it
    private final PassengerService passengerService;

    /**
     * Create a new passenger record.
     * Path: POST /api/v1/passengers
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Passenger>> addPassenger(@RequestBody PassengerRequest passengerRequest) {
        Passenger passenger = passengerService.addPassenger(passengerRequest);
        return ApiResponse.created(passenger, "Passenger Recorded Successfully");
    }

    /**
     * Get all passengers.
     * Fixed: Removed "/getAllPassengers" RPC path.
     * Path: GET /api/v1/passengers
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Passenger>>> getAllPassengers() {
        List<Passenger> passengers = passengerService.getAllPassengers();
        return ApiResponse.ok(passengers, "All Passengers Retrieved Successfully");
    }

    /**
     * Get a single passenger by ID.
     * Path: GET /api/v1/passengers/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Passenger>> getPassenger(@PathVariable int id) {
        return passengerService.getPassengerById(id)
                .map(passenger -> ApiResponse.ok(passenger, "Passenger Found"))
                // Pro-Tip: Ensure ApiResponse has a matching .status(HttpStatus.NOT_FOUND) helper
                .orElseGet(() -> ApiResponse.notFound("Passenger with ID " + id + " not found"));
    }
}