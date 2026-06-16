package com.praveen.simulator.service;

import com.praveen.simulator.entity.APP;
import com.praveen.simulator.repository.APPRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
// Enforce strict read-only transaction tuning across all query routes
@Transactional(readOnly = true)
public class AppService {

    private final APPRepository appRepository;

    /**
     * Fetch all historical and current APP data records.
     */
    public List<APP> getAllAPPData() {
        log.debug("Fetching complete collection of APP records from persistent store...");
        return appRepository.findAll();
    }

    /**
     * Fetch a single APP record via its primary artificial auto-increment Database ID.
     */
    public Optional<APP> getAppDataById(int id) {
        log.debug("Executing database lookup for primary APP record ID: {}", id);
        return appRepository.findById(id);
    }

    /**
     * Fetch an APP record via its unique functional Business System Identifier (appId).
     */
    public Optional<APP> getAppDataByAppId(String appId) {
        log.debug("Executing lookup matching unique business APP ID: {}", appId);
        return appRepository.findByAppId(appId);
    }

    /**
     * Fetch an APP record associated with a specific passenger PNR locator code.
     */
    public Optional<APP> getAppDataByPnrId(String pnrId) {
        log.debug("Executing lookup matching associated PNR reference: {}", pnrId);
        return appRepository.findByPnrId(pnrId);
    }

    /**
     * Resolves advanced passenger queries using either Border Clearance or Passenger identifiers.
     * Fixed: Handled structural boundary to prevent dangerous falling through to null queries.
     */
    public Optional<APP> searchAppData(String clearanceId, String passengerId) {
        log.debug("Evaluating secure app-data search. Clearance ID: {}, Passenger ID: {}", clearanceId, passengerId);

        if (clearanceId != null && !clearanceId.isBlank()) {
            return appRepository.findByGovernmentClearanceResponse_ClearanceId(clearanceId);
        }

        if (passengerId != null && !passengerId.isBlank()) {
            return appRepository.findByGovernmentClearanceResponse_PassengerId(passengerId);
        }

        // Production Safeguard: If someone calls /search with no valid arguments, exit cleanly
        log.warn("Search attempt blocked: Clearance ID and Passenger ID criteria are both missing or empty.");
        return Optional.empty();
    }
}
