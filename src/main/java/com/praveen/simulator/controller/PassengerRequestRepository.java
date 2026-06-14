package com.praveen.simulator.controller;

import com.praveen.simulator.dto.PassengerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRequestRepository extends JpaRepository<PassengerRequest,Integer> {
}
