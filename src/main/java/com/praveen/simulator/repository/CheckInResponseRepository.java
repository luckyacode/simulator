package com.praveen.simulator.repository;

import com.praveen.simulator.entity.CheckInResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CheckInResponseRepository extends JpaRepository<CheckInResponse,Integer> {
    Optional<CheckInResponse> findByGovernmentClearanceResponse_ClearanceId(String clearanceId);
    Optional<CheckInResponse> findByGovernmentClearanceResponse_PassengerId(String passengerId);

}
