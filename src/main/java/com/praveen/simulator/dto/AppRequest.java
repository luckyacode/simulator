package com.praveen.simulator.dto;

import com.praveen.simulator.model.PnrRecord;

public record AppRequest(
        PnrRecord pnr,
        String passportNumber,
        String issuingCountry,
        String gender
) {
}
