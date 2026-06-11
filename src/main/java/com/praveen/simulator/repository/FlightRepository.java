package com.praveen.simulator.repository;

import com.praveen.simulator.dto.Status;
import com.praveen.simulator.entity.FlightManifest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<FlightManifest,Integer> {

    List<FlightManifest> findAllByStatus(Status status);
    Optional<FlightManifest> findByFlightId(String flightId);
}
