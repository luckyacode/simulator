package com.praveen.simulator.service;

import com.praveen.simulator.entity.BookingStatus;
import com.praveen.simulator.entity.PNR;
import com.praveen.simulator.helper.AirlineException;
import com.praveen.simulator.repository.PnrRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PnrService {

    private final PnrRepository pnrRepository;

    /**
     * Retrieve a PNR record using its unique locator string ID.
     * Fixed: Optimized with readOnly transaction boundaries.
     */
    @Transactional(readOnly = true)
    public PNR getPnrById(String id) {
        log.debug("Searching PNR by record identifier: {}", id);
        return getOptionalPnrById(id)
                .orElseThrow(() -> AirlineException.notFound("PNR record locator '" + id + "' not found in system platform."));
    }

    /**
     * Low-level database call returning an Optional primitive context wrapper.
     */
    @Transactional(readOnly = true)
    public Optional<PNR> getOptionalPnrById(String id) {
        return pnrRepository.findByPnrId(id);
    }

    @Transactional
    public void updateBookingStatus(String pnrId, BookingStatus status) {
        log.info("Updating system booking status for PNR: {} to [{}]", pnrId, status);
        PNR pnr = getPnrById(pnrId);
        pnr.setBookingStatus(status);
        pnrRepository.save(pnr);
    }

}