package com.praveen.simulator.service;

import com.praveen.simulator.dto.FlightRequest;
import com.praveen.simulator.entity.FlightManifest;
import com.praveen.simulator.model.AppRecord;
import com.praveen.simulator.other.CommonMapper;
import com.praveen.simulator.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightService {
    private final FlightRepository flightRepository;
    private final CommonMapper commonMapper;

    public FlightManifest scheduleFlight(FlightRequest flightRequest) {
        log.info("Scheduling flight for : {}",flightRequest);
        FlightManifest flightManifest = commonMapper.toFlightManifest(flightRequest);
        log.info("Flight Scheduled : {}",flightManifest.getFlightId());
        return flightRepository.save(flightManifest);
    }

    public List<FlightManifest> getAllFlights() {
        log.info("Fetching All flights.... ");
        return flightRepository.findAll();
    }

    public List<FlightManifest> getActiveFlights() {
        log.info("Fetching Active flights.... ");
        return flightRepository.findAllByStatusActive();
    }
}
