package com.praveen.simulator.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GovernmentClearanceResponse {
    private String clearanceId;
    private String passengerId;
    private AuthorityDirection authorityDirective;   // OK (Clear), DNL (Do Not Board), CHCK (Manual Check)
    private String responseCode;         // System reason code (e.g., "00"=Approved, "91"=No Visa)
    private String denialReason;         // Human-readable message if blocked
    private LocalDateTime evaluatedAt;
}