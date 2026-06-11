package com.praveen.simulator.dto;

import java.time.LocalDate;

public record DocumentDetails(String passportNumber,
                              String documentType,
                              LocalDate documentExpiry,
                              String issuingCountry) {
}
