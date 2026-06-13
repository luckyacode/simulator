package com.praveen.simulator.controller;

import com.praveen.simulator.dto.FlightDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightDetailRepository extends JpaRepository<FlightDetail,Integer> {
}
