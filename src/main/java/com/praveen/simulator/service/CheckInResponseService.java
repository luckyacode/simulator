package com.praveen.simulator.service;

import com.praveen.simulator.entity.CheckInResponse;
import com.praveen.simulator.repository.CheckInResponseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CheckInResponseService {
    private final CheckInResponseRepository checkInResponseRepository;

    public void add(CheckInResponse checkInResponse){
        CheckInResponse response = checkInResponseRepository.save(checkInResponse);
        log.info("CheckInResponse saved to db for clearance {} and passenger {} , response is : {} ",checkInResponse.getGovernmentClearanceResponse().getClearanceId(),checkInResponse.getGovernmentClearanceResponse().getPassengerId(),checkInResponse);
    }

    public Optional<CheckInResponse> getCheckInResponseByClearanceId(String clearanceId) {
        log.info("Fetching checkInResponse for clearance : {}",clearanceId);
        return checkInResponseRepository.findByGovernmentClearanceResponse_ClearanceId(clearanceId);
    }

    public Optional<CheckInResponse> getCheckInResponseByPassengerId(String passengerId) {
        log.info("Fetching checkInResponse for Passenger : {}",passengerId);
        return checkInResponseRepository.findByGovernmentClearanceResponse_PassengerId(passengerId);
    }
}
