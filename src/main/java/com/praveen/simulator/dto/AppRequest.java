package com.praveen.simulator.dto;

import com.praveen.simulator.model.PnrRecord;

import java.time.LocalDate;

public record AppRequest(
        PNRRequest pnr,
        String appTransactionId,
        DocumentDetails documentDetails
//        GovernmentClearance governmentClearance
        ) {
}

