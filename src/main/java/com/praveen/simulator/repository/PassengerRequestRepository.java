package com.praveen.simulator.repository;

import com.praveen.simulator.dto.PassengerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRequestRepository extends JpaRepository<PassengerRequest,Integer> {
}
