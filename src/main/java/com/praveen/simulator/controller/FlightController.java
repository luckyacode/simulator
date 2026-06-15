package com.praveen.simulator.controller;

import com.praveen.simulator.dto.ApiResponse;
import com.praveen.simulator.dto.FlightRequest;
import com.praveen.simulator.dto.Status;
import com.praveen.simulator.entity.FlightManifest;
import com.praveen.simulator.service.FlightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/flights") // Production Standard: Versioning and Base Path
public class FlightController {

    private final FlightService flightService;

    // Production Standard: Constructor Injection instead of @Autowired
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    /**
     * Creation/Action: Creates a new flight schedule.
     * Uses POST and returns HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<ApiResponse<FlightManifest>> scheduleFlight(@RequestBody FlightRequest flightRequest) {
        // Production Note: For creation, use your .created() method (returns HTTP 201)
        return ApiResponse.created(flightService.scheduleFlight(flightRequest), "Flight Scheduling Triggered");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FlightManifest>>> getFlights() {
        return ApiResponse.ok(flightService.getAllFlights(), "All flights loaded successfully");
    }

    @GetMapping("/flightStatus")
    public ResponseEntity<ApiResponse<List<FlightManifest>>> getFlightsByStatus(
            @RequestParam Status status) {
        return ApiResponse.ok(flightService.getFlightsByStatus(status), "Flight with status "+status+" loaded successfully");
    }

    /**
     * Read Single: Fetches a flight by its unique ID.
     * URL looks like: GET /api/v1/flights/FL-102
     */
    @GetMapping("/{flightId}")
    public ResponseEntity<ApiResponse<FlightManifest>> getFlightById(@PathVariable String flightId) {
        return ApiResponse.ok(flightService.getFlightByFlightId(flightId), "Flight Found");
    }
}