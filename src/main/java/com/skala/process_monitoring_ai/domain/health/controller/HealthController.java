package com.skala.process_monitoring_ai.domain.health.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.skala.process_monitoring_ai.domain.health.service.HealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {

    private final HealthService healthService;

    @GetMapping
    public ResponseEntity<JsonNode> getHealth() {
        return ResponseEntity.ok(healthService.getHealth());
    }
}
