package com.praveen.simulator.entity;

public enum BookingStatus {

    // --- PHASE 1: RESERVATION & PAYMENT ---
    /**
     * The booking has been initiated but payment is not yet confirmed
     */
    PENDING_PAYMENT("PAYMENT_DUE"),

    /**
     * The ticket is fully paid, confirmed, and active
     */
    CONFIRMED("ACTIVE"),

    /**
     * The booking was cancelled by the passenger or airline before travel
     */
    CANCELLED("INACTIVE"),

    // --- PHASE 2: RISK, SECURITY & COMPLIANCE ---
    /**
     * ⚠️ Security threat index match or system flag; passenger cannot proceed
     */
    SECURITY_BLOCKED("BLOCKED"),

    /**
     * ⚠️ Missing visa data, invalid passport expiry, or random immigration verification flag
     */
    GATE_CHECK_REQUIRED("DOC_VERIFICATION"),

    /**
     * Passenger is on the standby/waitlist (e.g., overbooked flight logic)
     */
    STANDBY("WAITLISTED"),

    // --- PHASE 3: AIRPORT DEPARTURE CONTROL (DCS) ---
    /**
     * Passenger has successfully checked in online or at a kiosk and has a seat assigned
     */
    CHECKED_IN("DCS_ACTIVE"),

    /**
     * The boarding pass barcode has been scanned at the physical departure gate
     */
    BOARDED("DEPARTED_GATE"),

    /**
     * The passenger checked in but failed to show up at the gate before closure
     */
    NO_SHOW("GATE_CLOSED"),

    // --- PHASE 4: POST-FLIGHT & EXCLUSIONS ---
    /**
     * The flight has landed, and the journey is successfully completed
     */
    FLOWN("ARCHIVED"),

    /**
     * The passenger was offloaded by the captain or gate staff due to behavioral or legal issues
     */
    OFFLOADED("REJECTED");

    private final String description;

    BookingStatus(String description) {
        this.description = description;
    }
}