package com.praveen.simulator.service;

import com.praveen.simulator.dto.AuthorityDirection;
import com.praveen.simulator.entity.CheckInResponse;
import com.praveen.simulator.entity.PNR;
import com.praveen.simulator.helper.AirlineException;
import com.praveen.simulator.repository.PnrRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PnrService {

    private final PnrRepository pnrRepository;

    public PNR getPnrById(String id) {
        return getOptionalPnrById(id).orElseThrow(() -> AirlineException.notFound("PNR with " + id + " not found"));
    }

    public Optional<PNR> getOptionalPnrById(String id)  {
        return pnrRepository.findByPnrId(id);
    }


    @SneakyThrows
    public void handleVettingResult(CheckInResponse response) {
        PNR pnr = getPnrById(response.getPnrId());
        var clearance = response.getGovernmentClearanceResponse();

        // 1. Check if the government issued a hard "Do Not Board" (DNL)
        if (AuthorityDirection.DNL==clearance.getAuthorityDirective()) {

            log.error("CRITICAL BORDER ALERT: Passenger ID {} has been DENIED boarding by government control! Reason: {}",
                    clearance.getPassengerId(),
                    clearance.getDenialReason());

            // 2. Execute database update to lock out this PNR state
            pnr.setBookingStatus("SECURITY_BLOCKED");
            pnrRepository.save(pnr);
            return;
        }

        // 4. Fallback path for alternative warning states
        if (AuthorityDirection.CHCK==clearance.getAuthorityDirective()) {
            log.warn("Manual document verification required at gate for Passenger ID: {}", clearance.getPassengerId());
            pnr.setBookingStatus("GATE_CHECK_REQUIRED");
            pnrRepository.save(pnr);
            return;
        }

        // 5. Success Path (OK)
        pnr.setBookingStatus("Cleared");
        pnrRepository.save(pnr);
        log.info("Passenger ID {} cleared. Ready for departure.", clearance.getPassengerId());
    }
}
