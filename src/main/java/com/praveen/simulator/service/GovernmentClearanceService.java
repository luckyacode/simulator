package com.praveen.simulator.service;

import com.praveen.simulator.dto.AuthorityDirection;
import com.praveen.simulator.entity.BookingStatus;
import com.praveen.simulator.entity.CheckInResponse;
import com.praveen.simulator.entity.PNR;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GovernmentClearanceService {
    private final PnrService pnrService;

    @Transactional
    public void handleVettingResult(CheckInResponse response) {
        log.info("Processing government vetting evaluation for PNR Locator: {}", response.getPnrId());

        if (response.getGovernmentClearanceResponse() == null) {
            log.error("Aborting vetting execution: Inbound clearance payload payload metadata is empty or corrupted.");
            return;
        }

        var clearance = response.getGovernmentClearanceResponse();

        if (AuthorityDirection.DNL == clearance.getAuthorityDirective()) {
            log.error("🚨 CRITICAL BORDER ALARM: Passenger ID {} has been DENIED boarding by government authority! Reason: {}",
                    clearance.getPassengerId(), clearance.getDenialReason());

            pnrService.updateBookingStatus(response.getPnrId(), BookingStatus.SECURITY_BLOCKED);
            return;
        }

        // 3. Evaluate conditional manual checks required at gate (CHCK)
        if (AuthorityDirection.CHCK == clearance.getAuthorityDirective()) {
            log.warn("⚠️ GATE VERIFICATION REQUIRED: Document/Visa validation required at terminal gate for Passenger ID: {}",
                    clearance.getPassengerId());

            pnrService.updateBookingStatus(response.getPnrId(),BookingStatus.GATE_CHECK_REQUIRED);

            return;
        }

        pnrService.updateBookingStatus(response.getPnrId(),BookingStatus.CONFIRMED);
        log.info("✅ Passenger ID {} cleared validation checks successfully. Ready for flight dispatch routing.", clearance.getPassengerId());
    }

}
