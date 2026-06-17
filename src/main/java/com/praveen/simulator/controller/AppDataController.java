package com.praveen.simulator.controller;

import com.praveen.simulator.dto.ApiResponse;
import com.praveen.simulator.entity.APP;
import com.praveen.simulator.helper.Utils;
import com.praveen.simulator.service.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/app-data") // Production Standard: Kebab-case plural noun for domain data
@RequiredArgsConstructor
public class AppDataController { // Renamed to match domain reality

    private final AppService appDataService;

    /**
     * Fetch all APP data records.
     * Path: GET /api/v1/app-data
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<APP>>> getAllAppData() {
        List<APP> dataList = appDataService.getAllAPPData();
        return ApiResponse.ok(dataList, "All APP Data Retrieved Successfully");
    }

    /**
     * Fetch a single APP data record by its primary domain ID.
     * Path: GET /api/v1/app-data/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<APP>> getById(@PathVariable String id) {
        return appDataService.getAppDataByAppId(id)
                .map(app -> ApiResponse.ok(app, "APP Data Found"))
                .orElseGet(() -> ApiResponse.notFound("APP Data with id " + id + " not found"));
    }

    /**
     * Fetch APP data by its associated PNR record ID.
     * Path: GET /api/v1/app-data/pnr/{pnrId}
     */
    @GetMapping("/pnr/{pnrId}")
    public ResponseEntity<ApiResponse<APP>> getByPnrId(@PathVariable String pnrId) {
        return appDataService.getAppDataByPnrId(pnrId)
                .map(app -> ApiResponse.ok(app, "APP Data Found via PNR"))
                .orElseGet(() -> ApiResponse.notFound("APP Data with PNR " + pnrId + " not found"));
    }

    /**
     * Search/Filter APP data dynamically by clearance ID or passenger ID.
     * Path: GET /api/v1/app-data/search?clearanceId=XYZ or ?passengerId=123
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<APP>> searchAppData(
            @RequestParam(value = "clearanceId", required = false) String clearanceId,
            @RequestParam(value = "passengerId", required = false) String passengerId) {

        return appDataService.searchAppData(clearanceId, passengerId)
                .map(app -> ApiResponse.ok(app, "Matching APP Data Found"))
                .orElseGet(() -> ApiResponse.notFound("No matching APP Data found for given criteria"));
    }

    /**
     * Fetch formatted UN/EDIFACT message string for a specific APP data record.
     * Path: GET /api/v1/app-data/{id}/edifact-message
     */
    @GetMapping("/{id}/edifact-message")
    public ResponseEntity<ApiResponse<String>> getEdifactMessage(@PathVariable int id) {
        return appDataService.getAppDataById(id)
                .map(app -> {
                    String edifactString = Utils.convertToAppEdifact(app);
                    return ApiResponse.ok(edifactString, "EDIFACT Message Generated Successfully");
                })
                .orElseGet(() -> ApiResponse.notFound("UN/EDIFACT generation failed: APP Data with id " + id + " not found"));
    }
}