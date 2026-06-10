package com.praveen.simulator.repository;

import com.praveen.simulator.entity.FlightManifest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<FlightManifest,Integer> {

    List<FlightManifest> findAllByStatus(String status);

}
