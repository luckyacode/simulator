package com.praveen.simulator.service;

import com.praveen.simulator.dto.FlightRequest;
import com.praveen.simulator.dto.Status;
import com.praveen.simulator.entity.FlightManifest;
import com.praveen.simulator.other.CommonMapper;
import com.praveen.simulator.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @SneakyThrows
    public FlightManifest getFlightByFlightId(String flightId) {
        log.info("Searching flight by id....: {}",flightId);
        return flightRepository.findByFlightId(flightId).orElseThrow(()-> new Exception("Flight not found with id : "+flightId));
    }

    public List<FlightManifest> getActiveFlights() {
        log.info("Fetching Active flights.... ");
        return flightRepository.findAllByStatus(Status.ACTIVE);
    }
}
