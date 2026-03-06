package com.skala.process_monitoring_ai.domain.dashboard.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.skala.process_monitoring_ai.domain.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<JsonNode> getStats(
            @RequestParam(defaultValue = "week") String period
    ) {
        return ResponseEntity.ok(dashboardService.getStats(period));
    }
}
