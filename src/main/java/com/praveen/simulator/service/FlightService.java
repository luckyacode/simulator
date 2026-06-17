package com.praveen.simulator.service;

import com.praveen.simulator.dto.FlightRequest;
import com.praveen.simulator.dto.Status;
import com.praveen.simulator.entity.FlightManifest;
import com.praveen.simulator.helper.AirlineException;
import com.praveen.simulator.helper.CommonMapper;
import com.praveen.simulator.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightService {

    private final FlightRepository flightRepository;
    private final CommonMapper commonMapper;

    @Transactional
    public FlightManifest scheduleFlight(FlightRequest flightRequest) {
        log.info("Checking existing scheduling status for Flight ID: {}", flightRequest.getFlightId());

        if (flightRepository.findByFlightId(flightRequest.getFlightId()).isPresent()) {
            throw AirlineException.conflict("Flight already scheduled with ID: " + flightRequest.getFlightId());
        }

        log.info("Mapping and initializing flight manifest details for ID: {}", flightRequest.getFlightId());
        FlightManifest flightManifest = commonMapper.toFlightManifest(flightRequest);

        FlightManifest savedManifest = flightRepository.save(flightManifest);
        log.info("Flight successfully scheduled and committed to system registry. ID: {}", savedManifest.getFlightId());
        return savedManifest;
    }

    @Transactional(readOnly = true)
    public List<FlightManifest> getAllFlights() {
        log.debug("Fetching total flight manifest records from database...");
        return flightRepository.findAll();
    }

    @Transactional(readOnly = true)
    public FlightManifest getFlightByFlightId(String flightId) {
        log.debug("Searching flight manifest by record identifier: {}", flightId);
        return flightRepository.findByFlightId(flightId)
                .orElseThrow(() -> AirlineException.badRequest("Flight manifest registry entry not found for ID: " + flightId));
    }

    @Transactional(readOnly = true)
    public List<FlightManifest> getFlightsByStatus(Status status) {
        log.debug("Filtering flight manifests matching operational status code: {}", status);
        return flightRepository.findAllByStatus(status);
    }
}